package me.filby.neptune.runescript.ast.expr

import com.google.common.base.MoreObjects
import me.filby.neptune.runescript.ast.AstVisitor
import java.util.*

public class JoinedStringExpression(public val parts: List<Expression>) : Expression() {

    override fun <R> accept(visitor: AstVisitor<R>): R {
        return visitor.visitJoinedStringExpression(this)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(parts)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other !is JoinedStringExpression) {
            return false
        }

        return parts == other.parts
    }

    override fun toString(): String {
        return MoreObjects.toStringHelper(this)
            .add("parts", parts)
            .toString()
    }

}
