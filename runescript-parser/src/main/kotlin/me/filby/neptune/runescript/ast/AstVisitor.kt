package me.filby.neptune.runescript.ast

import me.filby.neptune.runescript.ast.expr.BinaryExpression
import me.filby.neptune.runescript.ast.expr.BooleanLiteral
import me.filby.neptune.runescript.ast.expr.CalcExpression
import me.filby.neptune.runescript.ast.expr.CharacterLiteral
import me.filby.neptune.runescript.ast.expr.Expression
import me.filby.neptune.runescript.ast.expr.Identifier
import me.filby.neptune.runescript.ast.expr.IntegerLiteral
import me.filby.neptune.runescript.ast.expr.Literal
import me.filby.neptune.runescript.ast.expr.NullLiteral
import me.filby.neptune.runescript.ast.statement.ExpressionStatement
import me.filby.neptune.runescript.ast.statement.Statement

public interface AstVisitor<R> {

    public fun visitScriptFile(scriptFile: ScriptFile): R {
        return visitNode(scriptFile)
    }

    public fun visitScript(script: Script): R {
        return visitNode(script)
    }

    public fun visitExpressionStatement(expressionStatement: ExpressionStatement): R {
        return visitStatement(expressionStatement)
    }

    public fun visitStatement(statement: Statement): R {
        return visitNode(statement)
    }

    public fun visitBinaryExpression(binaryExpression: BinaryExpression): R {
        return visitExpression(binaryExpression)
    }

    public fun visitCalcExpression(calcExpression: CalcExpression): R {
        return visitExpression(calcExpression)
    }

    public fun visitExpression(expression: Expression): R {
        return visitNode(expression)
    }

    public fun visitIntegerLiteral(integerLiteral: IntegerLiteral): R {
        return visitLiteral(integerLiteral)
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

    public fun visitLiteral(literal: Literal<*>): R {
        return visitExpression(literal)
    }

    public fun visitIdentifier(identifier: Identifier): R {
        return visitExpression(identifier)
    }

    public fun visitNode(node: Node): R {
        throw UnsupportedOperationException("not implemented: $node")
    }

}
