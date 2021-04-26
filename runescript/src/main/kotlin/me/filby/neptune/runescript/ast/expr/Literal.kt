package me.filby.neptune.runescript.ast.expr

import com.google.common.base.MoreObjects
import me.filby.neptune.runescript.ast.AstVisitor
import me.filby.neptune.runescript.ast.Node
import java.util.*

// base literal class that has a constant value
public sealed class Literal<T>(public val value: T) : Node() {

    override fun hashCode(): Int {
        return Objects.hashCode(value)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other !is Literal<*>) {
            return false;
        }

        return Objects.equals(value, other.value)
    }

    override fun toString(): String {
        return MoreObjects.toStringHelper(this)
            .add("value", value)
            .toString()
    }

}

public class IntegerLiteral(value: Int) : Literal<Int>(value) {

    override fun <R> accept(visitor: AstVisitor<R>): R {
        return visitor.visitIntegerLiteral(this)
    }

}
