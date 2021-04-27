package me.filby.neptune.runescript.ast.expr

import com.google.common.base.MoreObjects
import me.filby.neptune.runescript.ast.AstVisitor
import java.util.*

public class CalcExpression(public val expression: Expression) : Expression() {

    override fun <R> accept(visitor: AstVisitor<R>): R {
        return visitor.visitCalcExpression(this)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(expression)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other !is CalcExpression) {
            return false
        }

        return Objects.equals(expression, other.expression)
    }

    override fun toString(): String {
        return MoreObjects.toStringHelper(this)
            .add("expression", expression)
            .toString()
    }

}