package me.filby.neptune.runescript.ast.statement

import com.google.common.base.MoreObjects
import me.filby.neptune.runescript.ast.AstVisitor
import me.filby.neptune.runescript.ast.expr.Expression
import me.filby.neptune.runescript.ast.expr.VariableExpression
import java.util.*

public class AssignmentStatement(
    public val vars: List<VariableExpression>,
    public val expressions: List<Expression>
) : Statement() {

    override fun <R> accept(visitor: AstVisitor<R>): R {
        return visitor.visitAssignmentStatement(this)
    }

    override fun hashCode(): Int {
        return Objects.hash(vars, expressions)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other !is AssignmentStatement) {
            return false
        }

        return vars == other.vars
            && expressions == other.expressions
    }

    override fun toString(): String {
        return MoreObjects.toStringHelper(this)
            .add("vars", vars)
            .add("expressions", expressions)
            .toString()
    }

}