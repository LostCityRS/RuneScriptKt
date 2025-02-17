package me.filby.neptune.runescript.ast

import me.filby.neptune.runescript.ast.expr.ArithmeticExpression
import me.filby.neptune.runescript.ast.expr.BinaryExpression
import me.filby.neptune.runescript.ast.expr.BooleanLiteral
import me.filby.neptune.runescript.ast.expr.CalcExpression
import me.filby.neptune.runescript.ast.expr.CallExpression
import me.filby.neptune.runescript.ast.expr.CharacterLiteral
import me.filby.neptune.runescript.ast.expr.ClientScriptExpression
import me.filby.neptune.runescript.ast.expr.CommandCallExpression
import me.filby.neptune.runescript.ast.expr.ConditionExpression
import me.filby.neptune.runescript.ast.expr.ConstantVariableExpression
import me.filby.neptune.runescript.ast.expr.CoordLiteral
import me.filby.neptune.runescript.ast.expr.Expression
import me.filby.neptune.runescript.ast.expr.GameVariableExpression
import me.filby.neptune.runescript.ast.expr.Identifier
import me.filby.neptune.runescript.ast.expr.IntegerLiteral
import me.filby.neptune.runescript.ast.expr.JoinedStringExpression
import me.filby.neptune.runescript.ast.expr.JumpCallExpression
import me.filby.neptune.runescript.ast.expr.Literal
import me.filby.neptune.runescript.ast.expr.LocalVariableExpression
import me.filby.neptune.runescript.ast.expr.NullLiteral
import me.filby.neptune.runescript.ast.expr.ParenthesizedExpression
import me.filby.neptune.runescript.ast.expr.ProcCallExpression
import me.filby.neptune.runescript.ast.expr.StringLiteral
import me.filby.neptune.runescript.ast.expr.StringPart
import me.filby.neptune.runescript.ast.expr.VariableExpression
import me.filby.neptune.runescript.ast.statement.ArrayDeclarationStatement
import me.filby.neptune.runescript.ast.statement.AssignmentStatement
import me.filby.neptune.runescript.ast.statement.BlockStatement
import me.filby.neptune.runescript.ast.statement.DeclarationStatement
import me.filby.neptune.runescript.ast.statement.EmptyStatement
import me.filby.neptune.runescript.ast.statement.ExpressionStatement
import me.filby.neptune.runescript.ast.statement.IfStatement
import me.filby.neptune.runescript.ast.statement.ReturnStatement
import me.filby.neptune.runescript.ast.statement.Statement
import me.filby.neptune.runescript.ast.statement.SwitchCase
import me.filby.neptune.runescript.ast.statement.SwitchStatement
import me.filby.neptune.runescript.ast.statement.WhileStatement

public interface AstVisitor<R> {
    public fun visitScriptFile(scriptFile: ScriptFile): R {
        return visitNode(scriptFile)
    }

    public fun visitScript(script: Script): R {
        return visitNode(script)
    }

    public fun visitParameter(parameter: Parameter): R {
        return visitNode(parameter)
    }

    public fun visitBlockStatement(blockStatement: BlockStatement): R {
        return visitStatement(blockStatement)
    }

    public fun visitReturnStatement(returnStatement: ReturnStatement): R {
        return visitStatement(returnStatement)
    }

    public fun visitIfStatement(ifStatement: IfStatement): R {
        return visitStatement(ifStatement)
    }

    public fun visitWhileStatement(whileStatement: WhileStatement): R {
        return visitStatement(whileStatement)
    }

    public fun visitSwitchStatement(switchStatement: SwitchStatement): R {
        return visitStatement(switchStatement)
    }

    public fun visitSwitchCase(switchCase: SwitchCase): R {
        return visitNode(switchCase)
    }

    public fun visitDeclarationStatement(declarationStatement: DeclarationStatement): R {
        return visitStatement(declarationStatement)
    }

    public fun visitArrayDeclarationStatement(arrayDeclarationStatement: ArrayDeclarationStatement): R {
        return visitStatement(arrayDeclarationStatement)
    }

    public fun visitAssignmentStatement(assignmentStatement: AssignmentStatement): R {
        return visitStatement(assignmentStatement)
    }

    public fun visitExpressionStatement(expressionStatement: ExpressionStatement): R {
        return visitStatement(expressionStatement)
    }

    public fun visitEmptyStatement(emptyStatement: EmptyStatement): R {
        return visitStatement(emptyStatement)
    }

    public fun visitStatement(statement: Statement): R {
        return visitNode(statement)
    }

    public fun visitParenthesizedExpression(parenthesizedExpression: ParenthesizedExpression): R {
        return visitExpression(parenthesizedExpression)
    }

    public fun visitConditionExpression(conditionExpression: ConditionExpression): R {
        return visitBinaryExpression(conditionExpression)
    }

    public fun visitArithmeticExpression(arithmeticExpression: ArithmeticExpression): R {
        return visitBinaryExpression(arithmeticExpression)
    }

    public fun visitBinaryExpression(binaryExpression: BinaryExpression): R {
        return visitExpression(binaryExpression)
    }

    public fun visitCalcExpression(calcExpression: CalcExpression): R {
        return visitExpression(calcExpression)
    }

    public fun visitCommandCallExpression(commandCallExpression: CommandCallExpression): R {
        return visitCallExpression(commandCallExpression)
    }

    public fun visitProcCallExpression(procCallExpression: ProcCallExpression): R {
        return visitCallExpression(procCallExpression)
    }

    public fun visitJumpCallExpression(jumpCallExpression: JumpCallExpression): R {
        return visitCallExpression(jumpCallExpression)
    }

    public fun visitCallExpression(callExpression: CallExpression): R {
        return visitExpression(callExpression)
    }

    public fun visitClientScriptExpression(clientScriptExpression: ClientScriptExpression): R {
        return visitExpression(clientScriptExpression)
    }

    public fun visitLocalVariableExpression(localVariableExpression: LocalVariableExpression): R {
        return visitVariableExpression(localVariableExpression)
    }

    public fun visitGameVariableExpression(gameVariableExpression: GameVariableExpression): R {
        return visitVariableExpression(gameVariableExpression)
    }

    public fun visitConstantVariableExpression(constantVariableExpression: ConstantVariableExpression): R {
        return visitVariableExpression(constantVariableExpression)
    }

    public fun visitVariableExpression(variableExpression: VariableExpression): R {
        return visitExpression(variableExpression)
    }

    public fun visitExpression(expression: Expression): R {
        return visitNode(expression)
    }

    public fun visitIntegerLiteral(integerLiteral: IntegerLiteral): R {
        return visitLiteral(integerLiteral)
    }

    public fun visitCoordLiteral(coordLiteral: CoordLiteral): R {
        return visitLiteral(coordLiteral)
    }

    public fun visitBooleanLiteral(booleanLiteral: BooleanLiteral): R {
        return visitLiteral(booleanLiteral)
    }

    public fun visitCharacterLiteral(characterLiteral: CharacterLiteral): R {
        return visitLiteral(characterLiteral)
    }

    public fun visitNullLiteral(nullLiteral: NullLiteral): R {
        return visitLiteral(nullLiteral)
    }

    public fun visitStringLiteral(stringLiteral: StringLiteral): R {
        return visitLiteral(stringLiteral)
    }

    public fun visitLiteral(literal: Literal<*>): R {
        return visitExpression(literal)
    }

    public fun visitJoinedStringExpression(joinedStringExpression: JoinedStringExpression): R {
        return visitExpression(joinedStringExpression)
    }

    public fun visitJoinedStringPart(stringPart: StringPart): R {
        return visitNode(stringPart)
    }

    public fun visitIdentifier(identifier: Identifier): R {
        return visitExpression(identifier)
    }

    public fun visitToken(token: Token): R {
        return visitNode(token)
    }

    public fun visitNode(node: Node): R {
        throw UnsupportedOperationException("not implemented: ${node::class.simpleName}")
    }
}
