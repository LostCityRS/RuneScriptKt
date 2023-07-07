package me.filby.neptune.clientscript.compiler.command

import me.filby.neptune.clientscript.compiler.type.ParamType
import me.filby.neptune.runescript.compiler.configuration.command.DynamicCommandHandler
import me.filby.neptune.runescript.compiler.configuration.command.TypeCheckingContext
import me.filby.neptune.runescript.compiler.type
import me.filby.neptune.runescript.compiler.type.MetaType
import me.filby.neptune.runescript.compiler.type.TupleType
import me.filby.neptune.runescript.compiler.type.Type

class ParamCommandHandler(private val type: Type?) : DynamicCommandHandler {
    override fun TypeCheckingContext.typeCheck() {
        val expectedTypes = mutableListOf<Type>()

        if (type != null) {
            expectedTypes += type
            checkArgument(0, type)
        }

        // check the second argument is a param reference
        val paramExpr = checkArgument(if (type == null) 0 else 1, PARAM_ANY)
        val paramReturnType = (paramExpr?.type as? ParamType)?.inner

        // add param type to the expected types list
        expectedTypes += ParamType(paramReturnType ?: MetaType.Any)

        // compare the expected types with the actual types
        if (!checkArgumentTypes(TupleType.fromList(expectedTypes))) {
            expression.type = MetaType.Error
            return
        }

        // verify the param type was defined
        if (paramReturnType == null) {
            expression.reportError(diagnostics, "Param return type was not able to be found.")
            expression.type = MetaType.Error
            return
        }

        expression.type = paramReturnType
    }

    private companion object {
        val PARAM_ANY = ParamType(MetaType.Any)
    }
}
