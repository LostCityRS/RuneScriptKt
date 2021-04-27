package me.filby.neptune.runescript.ast.expr

import com.google.common.base.MoreObjects
import me.filby.neptune.runescript.ast.AstVisitor
import java.util.*

public class BinaryExpression(
    public val left: Expression,
    public val operator: String,
    public val right: Expression
) : Expression() {

    override fun <R> accept(visitor: AstVisitor<R>): R {
        return visitor.visitBinaryExpression(this)
    }

    override fun hashCode(): Int {
        return Objects.hash(left, operator, right)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other !is BinaryExpression) {
            return false
        }

        return Objects.equals(left, other.left)
            && Objects.equals(operator, other.operator)
            && Objects.equals(right, other.right)
    }

    override fun toString(): String {
        return MoreObjects.toStringHelper(this)
            .add("left", left)
            .add("operator", operator)
            .add("right", right)
            .toString()
    }

}