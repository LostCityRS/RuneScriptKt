package me.filby.neptune.runescript.compiler.semantics

import me.filby.neptune.runescript.ast.AstVisitor
import me.filby.neptune.runescript.ast.Node
import me.filby.neptune.runescript.ast.Script
import me.filby.neptune.runescript.ast.ScriptFile
import me.filby.neptune.runescript.ast.Token
import me.filby.neptune.runescript.ast.expr.BinaryExpression
import me.filby.neptune.runescript.ast.expr.BooleanLiteral
import me.filby.neptune.runescript.ast.expr.CalcExpression
import me.filby.neptune.runescript.ast.expr.CharacterLiteral
import me.filby.neptune.runescript.ast.expr.ConstantVariableExpression
import me.filby.neptune.runescript.ast.expr.Expression
import me.filby.neptune.runescript.ast.expr.Identifier
import me.filby.neptune.runescript.ast.expr.IntegerLiteral
import me.filby.neptune.runescript.ast.expr.JoinedStringExpression
import me.filby.neptune.runescript.ast.expr.JumpCallExpression
import me.filby.neptune.runescript.ast.expr.Literal
import me.filby.neptune.runescript.ast.expr.LocalVariableExpression
import me.filby.neptune.runescript.ast.expr.NullLiteral
import me.filby.neptune.runescript.ast.expr.ParenthesizedExpression
import me.filby.neptune.runescript.ast.expr.StringLiteral
import me.filby.neptune.runescript.ast.statement.ArrayDeclarationStatement
import me.filby.neptune.runescript.ast.statement.AssignmentStatement
import me.filby.neptune.runescript.ast.statement.BlockStatement
import me.filby.neptune.runescript.ast.statement.DeclarationStatement
import me.filby.neptune.runescript.ast.statement.EmptyStatement
import me.filby.neptune.runescript.ast.statement.ExpressionStatement
import me.filby.neptune.runescript.ast.statement.IfStatement
import me.filby.neptune.runescript.ast.statement.SwitchCase
import me.filby.neptune.runescript.ast.statement.SwitchStatement
import me.filby.neptune.runescript.ast.statement.WhileStatement
import me.filby.neptune.runescript.compiler.diagnostics.Diagnostic
import me.filby.neptune.runescript.compiler.diagnostics.DiagnosticMessage
import me.filby.neptune.runescript.compiler.diagnostics.DiagnosticType
import me.filby.neptune.runescript.compiler.diagnostics.Diagnostics
import me.filby.neptune.runescript.compiler.nullableType
import me.filby.neptune.runescript.compiler.reference
import me.filby.neptune.runescript.compiler.symbol
import me.filby.neptune.runescript.compiler.symbol.BasicSymbol
import me.filby.neptune.runescript.compiler.symbol.ClientScriptSymbol
import me.filby.neptune.runescript.compiler.symbol.ComponentSymbol
import me.filby.neptune.runescript.compiler.symbol.ConfigSymbol
import me.filby.neptune.runescript.compiler.symbol.ConstantSymbol
import me.filby.neptune.runescript.compiler.symbol.LocalVariableSymbol
import me.filby.neptune.runescript.compiler.symbol.ServerScriptSymbol
import me.filby.neptune.runescript.compiler.symbol.Symbol
import me.filby.neptune.runescript.compiler.symbol.SymbolTable
import me.filby.neptune.runescript.compiler.symbol.SymbolType
import me.filby.neptune.runescript.compiler.type
import me.filby.neptune.runescript.compiler.type.ArrayType
import me.filby.neptune.runescript.compiler.type.BaseVarType
import me.filby.neptune.runescript.compiler.type.MetaType
import me.filby.neptune.runescript.compiler.type.PrimitiveType
import me.filby.neptune.runescript.compiler.type.TupleType
import me.filby.neptune.runescript.compiler.type.Type
import me.filby.neptune.runescript.compiler.typeHint

internal class TypeChecking(
    private val rootTable: SymbolTable,
    private val diagnostics: Diagnostics
) : AstVisitor<Unit> {
    override fun visitScriptFile(scriptFile: ScriptFile) {
        // visit all scripts in the file
        scriptFile.scripts.visit()
    }

    override fun visitScript(script: Script) {
        // visit all statements, we don't need to do anything else with the script
        // since all the other stuff is handled in pre-type checking.
        script.statements.visit()
    }

    override fun visitBlockStatement(blockStatement: BlockStatement) {
        // visit all statements
        blockStatement.statements.visit()
    }

    override fun visitIfStatement(ifStatement: IfStatement) {
        checkCondition(ifStatement.condition)
        ifStatement.thenStatement.visit()
        ifStatement.elseStatement?.visit()
    }

    override fun visitWhileStatement(whileStatement: WhileStatement) {
        checkCondition(whileStatement.condition)
        whileStatement.thenStatement.visit()
    }

    /**
     * Handles type hinting and visiting the expression, then checking if it is a valid conditional
     * expression. If the condition returns anything other than `boolean`, or is not a valid
     * condition expression, an error is emitted.
     */
    private fun checkCondition(expression: Expression) {
        // type hint and visit condition
        expression.typeHint = PrimitiveType.BOOLEAN
        expression.visit()

        // verify the condition expression is a binary expression or one wrapped in parentheses.
        if (isValidConditionExpression(expression)) {
            checkTypeMatch(expression, PrimitiveType.BOOLEAN, expression.type)
        } else {
            // report invalid condition expression
            expression.reportError(DiagnosticMessage.CONDITION_INVALID_NODE_TYPE)
        }
    }

    /**
     * Checks if a give expression is a valid conditional expression. The expression is only valid
     * if the expression is [BinaryExpression] or a [ParenthesizedExpression] with a
     * [BinaryExpression]. If the expression is a [ParenthesizedExpression] it recursively calls
     * this function until it finds a non-[ParenthesizedExpression].
     */
    private fun isValidConditionExpression(expression: Expression): Boolean = when (expression) {
        is BinaryExpression -> true
        is ParenthesizedExpression -> isValidConditionExpression(expression.expression)
        else -> false
    }

    override fun visitSwitchStatement(switchStatement: SwitchStatement) {
        val expectedType = switchStatement.type

        // type hint the condition and visit it
        val condition = switchStatement.condition
        condition.typeHint = expectedType
        condition.visit()
        checkTypeMatch(condition, expectedType, condition.type)

        // TODO check for duplicate case labels (other than default)
        // visit all the cases, cases will be type checked there.
        var defaultCase: SwitchCase? = null
        for (case in switchStatement.cases) {
            if (case.isDefault) {
                if (defaultCase == null) {
                    defaultCase = case
                } else {
                    case.reportError(DiagnosticMessage.SWITCH_DUPLICATE_DEFAULT)
                }
            }
            case.visit()
        }
    }

    override fun visitSwitchCase(switchCase: SwitchCase) {
        val switchType = (switchCase.parent as? SwitchStatement)?.type
        if (switchType == null) {
            // the parent should always be a switch statement, if not we're in trouble...
            switchCase.reportError(DiagnosticMessage.CASE_WITHOUT_SWITCH)
            return
        }

        // visit the case keys
        for (key in switchCase.keys) {
            // type hint and visit so we can access more information in constant expression check
            key.typeHint = switchType
            key.visit()

            if (!isConstantExpression(key)) {
                key.reportError(DiagnosticMessage.SWITCH_CASE_NOT_CONSTANT)
                continue
            }

            // expression is a constant, now we need to verify the types match
            checkTypeMatch(key, switchType, key.type)
        }

        // visit the statements
        switchCase.statements.visit()
    }

    /**
     * Checks if the result of [expression] is known at compile time.
     */
    private fun isConstantExpression(expression: Expression): Boolean = when (expression) {
        is ConstantVariableExpression -> true
        is Literal<*> -> true
        is Identifier -> {
            val ref = expression.reference
            ref != null && isConstantSymbol(ref)
        }
        else -> false
    }

    /**
     * Checks if the value of [symbol] is known at compile time.
     */
    private fun isConstantSymbol(symbol: Symbol): Boolean = when (symbol) {
        is BasicSymbol -> true
        is ConstantSymbol -> true
        is ConfigSymbol -> true
        is ComponentSymbol -> true
        else -> false
    }

    override fun visitDeclarationStatement(declarationStatement: DeclarationStatement) {
        val initializer = declarationStatement.initializer
        if (initializer != null) {
            val symbol = declarationStatement.symbol

            // type hint that we want whatever the declarations type is then visit
            initializer.typeHint = symbol.type
            initializer.visit()

            checkTypeMatch(initializer, symbol.type, initializer.type)
        }
    }

    override fun visitArrayDeclarationStatement(arrayDeclarationStatement: ArrayDeclarationStatement) {
        val initializer = arrayDeclarationStatement.initializer
        initializer.visit()
        checkTypeMatch(initializer, PrimitiveType.INT, initializer.type)
    }

    override fun visitAssignmentStatement(assignmentStatement: AssignmentStatement) {
        // visit the lhs to fetch the references
        assignmentStatement.vars.visit()

        // store the lhs types to help with type hinting
        val leftTypes = assignmentStatement.vars.map { it.type }
        val rightTypes = mutableListOf<Type>()
        var typeCounter = 0
        for (expr in assignmentStatement.expressions) {
            expr.typeHint = if (typeCounter < leftTypes.size) leftTypes[typeCounter] else null
            expr.visit()

            // add the evaluated type
            rightTypes += expr.type

            // increment the counter for type hinting
            typeCounter += if (expr.type is TupleType) {
                (expr.type as TupleType).children.size
            } else {
                1
            }
        }

        // convert types to tuple type if necessary for easy comparison
        val leftType = TupleType.fromList(leftTypes)
        val rightType = TupleType.fromList(rightTypes)
        if (leftType == null || rightType == null) {
            assignmentStatement.reportError(
                DiagnosticMessage.NULL_TYPE_IN_ASSIGNMENT,
                leftType?.representation ?: "null",
                rightType?.representation ?: "null"
            )
            return
        }

        checkTypeMatch(assignmentStatement, leftType, rightType)
    }

    override fun visitExpressionStatement(expressionStatement: ExpressionStatement) {
        // just visit the inside expression
        expressionStatement.expression.visit()
    }

    override fun visitEmptyStatement(emptyStatement: EmptyStatement) {
        // NO-OP
    }

    override fun visitParenthesizedExpression(parenthesizedExpression: ParenthesizedExpression) {
        val innerExpression = parenthesizedExpression.expression

        // relay the type hint to the inner expression and visit it
        innerExpression.typeHint = parenthesizedExpression.typeHint
        innerExpression.visit()

        // set the type to the type of what the expression evaluates to
        parenthesizedExpression.type = innerExpression.type
    }

    override fun visitBinaryExpression(binaryExpression: BinaryExpression) {
        // hint should be either int for `calc` or boolean for conditions
        val typeHint = binaryExpression.typeHint

        // binary expressions are syntactically only possible within calc and a condition,
        // which requires with an int or boolean.
        if (typeHint != PrimitiveType.INT && typeHint != PrimitiveType.BOOLEAN) {
            binaryExpression.type = MetaType.ERROR
            binaryExpression.reportError(DiagnosticMessage.INVALID_BINARYEXPR_TYPEHINT, typeHint ?: "null")
            return
        }

        val left = binaryExpression.left
        val right = binaryExpression.right
        val operator = binaryExpression.operator

        // type hint should only ever be int or boolean here
        val validOperation = when (typeHint) {
            PrimitiveType.INT -> checkBinaryMathOperation(binaryExpression, left, operator, right)
            PrimitiveType.BOOLEAN -> checkBinaryConditionOperation(binaryExpression, left, operator, right)
            else -> false
        }

        // early return if it isn't a valid operation
        if (!validOperation) {
            binaryExpression.type = MetaType.ERROR
            return
        }

        binaryExpression.type = typeHint
    }

    /**
     * Verifies the binary expression is a valid math operation.
     */
    private fun checkBinaryMathOperation(
        parent: BinaryExpression,
        left: Expression,
        operator: String,
        right: Expression
    ): Boolean {
        if (operator !in MATH_OPS) {
            // TODO make operator a token so we can point to it in an error message
            parent.reportError(DiagnosticMessage.INVALID_MATHOP, operator)
            return false
        }

        // visit left-hand side
        left.typeHint = PrimitiveType.INT
        left.visit()

        // visit right-hand side
        right.typeHint = PrimitiveType.INT
        right.visit()

        // verify if both sides are ints
        var bothMatch = checkTypeMatch(left, PrimitiveType.INT, left.type, reportError = false)
        bothMatch = bothMatch and checkTypeMatch(right, PrimitiveType.INT, right.type, reportError = false)

        // one or both don't match so report an error
        if (!bothMatch) {
            // TODO make operator a token so we can point to it in an error message
            parent.reportError(
                DiagnosticMessage.BINOP_INVALID_TYPES,
                operator,
                left.type.representation,
                right.type.representation
            )
        }

        return bothMatch
    }

    /**
     * Verifies the binary expression is a valid condition operation.
     */
    private fun checkBinaryConditionOperation(
        parent: BinaryExpression,
        left: Expression,
        operator: String,
        right: Expression
    ): Boolean {
        if (operator !in CONDITIONAL_OPS) {
            // TODO make operator a token so we can point to it in an error message
            parent.reportError(DiagnosticMessage.INVALID_MATHOP, operator)
            return false
        }

        if (operator == "&" || operator == "|") {
            // assign boolean type hints since the operator is logical and or logical or,
            // which MUST evaluate to boolean
            left.typeHint = PrimitiveType.BOOLEAN
            right.typeHint = PrimitiveType.BOOLEAN
        } else {
            // assign the type hints using the opposite side if it isn't already assigned.
            left.typeHint = if (left.typeHint != null) left.typeHint else right.nullableType
            right.typeHint = if (right.typeHint != null) right.typeHint else left.nullableType
        }

        // visit both sides to evaluate types
        left.visit()
        right.visit()

        // check if either side is a tuple type. the runtime only allows comparing two values
        if (left.type is TupleType || right.type is TupleType) {
            if (left.type is TupleType) {
                left.reportError(DiagnosticMessage.BINOP_TUPLE_TYPE, "Left", left.type.representation)
            }
            if (right.type is TupleType) {
                right.reportError(DiagnosticMessage.BINOP_TUPLE_TYPE, "Right", right.type.representation)
            }
            return false
        }

        // verify if both sides are equal
        if (!checkTypeMatch(left, left.type, right.type, reportError = false)) {
            // TODO make operator a token so we can point to it in an error message
            parent.reportError(
                DiagnosticMessage.BINOP_INVALID_TYPES,
                operator,
                left.type.representation,
                right.type.representation
            )
            return false
        }

        // other cases are true
        return true
    }

    override fun visitCalcExpression(calcExpression: CalcExpression) {
        val innerExpression = calcExpression.expression

        // hint to the expression that we expect an int
        innerExpression.typeHint = PrimitiveType.INT
        innerExpression.visit()

        // verify type is an int
        if (!checkTypeMatch(innerExpression, PrimitiveType.INT, innerExpression.type)) {
            calcExpression.type = MetaType.ERROR
            return
        }

        calcExpression.type = PrimitiveType.INT
    }

    override fun visitJumpCallExpression(jumpCallExpression: JumpCallExpression) {
        jumpCallExpression.reportError(DiagnosticMessage.JUMP_CALL_IN_CS2, jumpCallExpression.name.text)
        // TODO some kind of meta type for no return?
        jumpCallExpression.type = MetaType.ERROR
    }

    /**
     * Type checks the index value expression if it is defined.
     */
    override fun visitLocalVariableExpression(localVariableExpression: LocalVariableExpression) {
        // if the reference is null, that means it failed to find the symbol associated with it
        // in pre-type checking and would have already been reported.
        val reference = localVariableExpression.reference ?: return
        if (reference !is LocalVariableSymbol) {
            localVariableExpression.reportError(
                DiagnosticMessage.LOCAL_REFERENCE_WRONG,
                reference.name,
                reference::class.simpleName!!
            )
            return
        }

        val indexExpression = localVariableExpression.index
        if (reference.type is ArrayType && indexExpression != null) {
            indexExpression.visit()
            checkTypeMatch(indexExpression, PrimitiveType.INT, indexExpression.type)
        }

        // type is set in PreTypeChecking
    }

    override fun visitConstantVariableExpression(constantVariableExpression: ConstantVariableExpression) {
        // NO-OP, constants are handled in pre-type checking.
    }

    override fun visitIntegerLiteral(integerLiteral: IntegerLiteral) {
        integerLiteral.type = PrimitiveType.INT
    }

    override fun visitBooleanLiteral(booleanLiteral: BooleanLiteral) {
        booleanLiteral.type = PrimitiveType.BOOLEAN
    }

    override fun visitCharacterLiteral(characterLiteral: CharacterLiteral) {
        characterLiteral.type = PrimitiveType.CHAR
    }

    override fun visitNullLiteral(nullLiteral: NullLiteral) {
        nullLiteral.type = MetaType.NULL
    }

    override fun visitStringLiteral(stringLiteral: StringLiteral) {
        val typeHint = stringLiteral.typeHint
        if (typeHint == PrimitiveType.GRAPHIC) {
            val symbol = rootTable.find(SymbolType.Basic(PrimitiveType.GRAPHIC), stringLiteral.value)
            if (symbol == null) {
                stringLiteral.type = MetaType.ERROR
                stringLiteral.reportError(DiagnosticMessage.GENERIC_UNRESOLVED_SYMBOL, stringLiteral.value)
                return
            }
            // TODO store symbol
            stringLiteral.type = PrimitiveType.GRAPHIC
            return
        }
        stringLiteral.type = PrimitiveType.STRING
    }

    override fun visitJoinedStringExpression(joinedStringExpression: JoinedStringExpression) {
        // visit the parts and verify they're all strings
        joinedStringExpression.parts.visit()
        for (part in joinedStringExpression.parts) {
            checkTypeMatch(part, PrimitiveType.STRING, part.type)
        }

        joinedStringExpression.type = PrimitiveType.STRING
    }

    override fun visitIdentifier(identifier: Identifier) {
        if (identifier.reference != null) {
            // handled already in pre-type checking for array references.
            return
        }

        val name = identifier.text
        var hint: Type? = identifier.typeHint

        // assume component if the name contains a colon
        if (hint == null && identifier.text.contains(":")) {
            hint = PrimitiveType.COMPONENT
        }

        // look through the global table for a symbol with the given name and type
        var symbol: Symbol? = null
        var symbolType: Type? = null
        for (temp in rootTable.findAll<Symbol>(name)) {
            // TODO filter specific types of symbols from this so they can't be accessed in the wrong way.
            val tempSymbolType = symbolToType(temp)
            if (hint == null || tempSymbolType != null && isTypeCompatible(hint, tempSymbolType)) {
                // hint type matches (or is null) so we can stop looking
                symbol = temp
                symbolType = tempSymbolType
                break
            } else if (symbol == null) {
                // default the symbol to the first thing found just in case
                // no exact matches exist
                symbol = temp
                symbolType = tempSymbolType
            }
        }

        if (symbol == null) {
            // unable to resolve the symbol
            identifier.type = MetaType.ERROR
            identifier.reportError(DiagnosticMessage.GENERIC_UNRESOLVED_SYMBOL, name)
            return
        }

        // identifier.reportInfo("hint=%s, symbol=%s", hint?.representation ?: "null", symbol)

        // compiler error if the symbol type isn't defined here
        if (symbolType == null) {
            identifier.type = MetaType.ERROR
            identifier.reportError(DiagnosticMessage.UNSUPPORTED_SYMBOLTYPE_TO_TYPE, symbol::class.java.simpleName)
            return
        }

        identifier.reference = symbol
        identifier.type = symbolType
    }

    /**
     * Converts a [Symbol] to its equivalent [Type].
     */
    private fun symbolToType(symbol: Symbol) = when (symbol) {
        is ServerScriptSymbol -> null
        is ClientScriptSymbol -> null
        is LocalVariableSymbol -> symbol.type
        is BasicSymbol -> symbol.type
        is ConstantSymbol -> symbol.type
        is ConfigSymbol -> symbol.type
        is ComponentSymbol -> PrimitiveType.COMPONENT
    }

    override fun visitNode(node: Node) {
        if (node !is Token) {
            val parent = node.parent
            if (parent == null) {
                node.reportInfo("Unhandled node: %s.", node::class.simpleName!!)
            } else {
                node.reportInfo("Unhandled node: %s. Parent: %s", node::class.simpleName!!, parent::class.simpleName!!)
            }
        }
    }

    /**
     * Checks if the [expected] and [actual] match, including accepted casting.
     *
     * If the types passed in are a [TupleType] they will be compared using their flattened types.
     *
     * @see isTypeCompatible
     */
    private fun checkTypeMatch(node: Node, expected: Type, actual: Type, reportError: Boolean = true): Boolean {
        val expectedFlattened = if (expected is TupleType) expected.children else arrayOf(expected)
        val actualFlattened = if (actual is TupleType) actual.children else arrayOf(actual)

        var match = true
        if (expectedFlattened.size != actualFlattened.size) {
            match = false
        } else {
            for (i in expectedFlattened.indices) {
                match = match and isTypeCompatible(expectedFlattened[i], actualFlattened[i])
            }
        }

        if (!match && reportError) {
            node.reportError(DiagnosticMessage.GENERIC_TYPE_MISMATCH, actual.representation, expected.representation)
        }
        return match
    }

    /**
     * Checks if the two types are compatible in other ways than equality. All types
     * are compatible with `undefined` type to help prevent error propagation.
     *
     * Example: `graphic`s can be assigned to `fontmetrics`.
     */
    private fun isTypeCompatible(first: Type, second: Type): Boolean {
        if (first == MetaType.ERROR || second == MetaType.ERROR) {
            // allow undefined to be compatible with anything to prevent error propagation
            return true
        }
        if (second == MetaType.NULL) {
            // any int based variable is nullable
            // TODO ability to configure this per type?
            return first.baseType == BaseVarType.INTEGER
        }
        if (first == PrimitiveType.OBJ) {
            // allow assigning namedobj to obj but not obj to namedobj
            return second == PrimitiveType.OBJ || second == PrimitiveType.NAMEDOBJ
        }
        return first == second
    }

    /**
     * Helper function to report a diagnostic with the type of [DiagnosticType.INFO].
     */
    private fun Node.reportInfo(message: String, vararg args: Any) {
        diagnostics.report(Diagnostic(DiagnosticType.INFO, this, message, *args))
    }

    /**
     * Helper function to report a diagnostic with the type of [DiagnosticType.WARNING].
     */
    private fun Node.reportWarning(message: String, vararg args: Any) {
        diagnostics.report(Diagnostic(DiagnosticType.WARNING, this, message, *args))
    }

    /**
     * Helper function to report a diagnostic with the type of [DiagnosticType.ERROR].
     */
    private fun Node.reportError(message: String, vararg args: Any) {
        diagnostics.report(Diagnostic(DiagnosticType.ERROR, this, message, *args))
    }

    /**
     * Shortcut to [Node.accept] for nullable nodes.
     */
    private fun Node?.visit() {
        this ?: return
        accept(this@TypeChecking)
    }

    /**
     * Calls [Node.accept] on all nodes in a list.
     */
    private fun List<Node>?.visit() {
        this ?: return
        for (n in this) {
            n.visit()
        }
    }

    private companion object {
        /**
         * Array of valid math operations allowed within `calc` expressions.
         */
        private val MATH_OPS = arrayOf(
            "*", "/", "%",
            "+", "-",
            "&", "|"
        )

        /**
         * Array of valid conditional operations allowed within `if` and `while` statements.
         */
        private val CONDITIONAL_OPS = arrayOf(
            "<", ">", "<=", ">=",
            "=", "!",
            "&", "|"
        )
    }
}
