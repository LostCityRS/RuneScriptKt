package me.filby.neptune.serverscript.compiler.util

import me.filby.neptune.runescript.ast.AstVisitor
import me.filby.neptune.runescript.ast.Node
import me.filby.neptune.runescript.ast.expr.BasicStringPart
import me.filby.neptune.runescript.ast.expr.BinaryExpression
import me.filby.neptune.runescript.ast.expr.CalcExpression
import me.filby.neptune.runescript.ast.expr.CharacterLiteral
import me.filby.neptune.runescript.ast.expr.CommandCallExpression
import me.filby.neptune.runescript.ast.expr.ConstantVariableExpression
import me.filby.neptune.runescript.ast.expr.ExpressionStringPart
import me.filby.neptune.runescript.ast.expr.GameVariableExpression
import me.filby.neptune.runescript.ast.expr.Identifier
import me.filby.neptune.runescript.ast.expr.JoinedStringExpression
import me.filby.neptune.runescript.ast.expr.Literal
import me.filby.neptune.runescript.ast.expr.LocalVariableExpression
import me.filby.neptune.runescript.ast.expr.NullLiteral
import me.filby.neptune.runescript.ast.expr.ProcCallExpression
import me.filby.neptune.runescript.ast.expr.StringLiteral
import me.filby.neptune.runescript.ast.expr.StringPart

class ExpressionGenerator : AstVisitor<String> {
    override fun visitBinaryExpression(binaryExpression: BinaryExpression): String {
        return "${binaryExpression.left.visit()} ${binaryExpression.operator.text} ${binaryExpression.right.visit()}"
    }

    override fun visitCalcExpression(calcExpression: CalcExpression): String {
        return "calc(${calcExpression.expression.visit()})"
    }

    override fun visitCommandCallExpression(commandCallExpression: CommandCallExpression) = buildString {
        append(commandCallExpression.name.visit())
        if (commandCallExpression.arguments.isNotEmpty()) {
            append('(')
            append(commandCallExpression.arguments.joinToString { it.visit() })
            append(')')
        }
    }

    override fun visitProcCallExpression(procCallExpression: ProcCallExpression) = buildString {
        append("~")
        append(procCallExpression.name.visit())
        if (procCallExpression.arguments.isNotEmpty()) {
            append('(')
            append(procCallExpression.arguments.joinToString { it.visit() })
            append(')')
        }
    }

    override fun visitLocalVariableExpression(localVariableExpression: LocalVariableExpression): String {
        return "${'$'}${localVariableExpression.name.visit()}"
    }

    override fun visitGameVariableExpression(gameVariableExpression: GameVariableExpression): String {
        return "%${gameVariableExpression.name.visit()}"
    }

    override fun visitConstantVariableExpression(constantVariableExpression: ConstantVariableExpression): String {
        return "^${constantVariableExpression.name.visit()}"
    }

    override fun visitCharacterLiteral(characterLiteral: CharacterLiteral): String {
        return "'${characterLiteral.value}'"
    }

    override fun visitNullLiteral(nullLiteral: NullLiteral): String {
        return "null"
    }

    override fun visitStringLiteral(stringLiteral: StringLiteral): String {
        return "\"${stringLiteral.value}\""
    }

    override fun visitLiteral(literal: Literal<*>): String {
        return literal.value.toString()
    }

    override fun visitJoinedStringExpression(joinedStringExpression: JoinedStringExpression) = buildString {
        append('"')
        for (part in joinedStringExpression.parts) {
            append(part.visit())
        }
        append('"')
    }

    override fun visitJoinedStringPart(stringPart: StringPart): String {
        return when (stringPart) {
            is BasicStringPart -> stringPart.value
            is ExpressionStringPart -> "<${stringPart.expression.visit()}>"
            else -> error("Unsupported StringPart: $stringPart")
        }
    }

    override fun visitIdentifier(identifier: Identifier): String {
        return identifier.text
    }

    private fun Node.visit(): String {
        return accept(this@ExpressionGenerator)
    }
}
