package me.filby.neptune.runescript.ast

import com.google.common.base.MoreObjects
import com.google.common.base.Objects
import me.filby.neptune.runescript.ScriptVarType
import me.filby.neptune.runescript.ast.expr.Identifier

public class Parameter(
    public val type: ScriptVarType,
    public val name: Identifier,
    public val isArray: Boolean = false
) : Node() {

    override fun <R> accept(visitor: AstVisitor<R>): R {
        return visitor.visitParameter(this)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(type, name, isArray)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other !is Parameter) {
            return false
        }

        return type == other.type
            && name == other.name
            && isArray == other.isArray
    }

    override fun toString(): String {
        return MoreObjects.toStringHelper(this)
            .add("type", type)
            .add("name", name)
            .add("isArray", isArray)
            .toString()
    }

}