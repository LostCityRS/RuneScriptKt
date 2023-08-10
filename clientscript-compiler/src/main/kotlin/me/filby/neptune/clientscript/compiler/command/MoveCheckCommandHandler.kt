package me.filby.neptune.clientscript.compiler.command

import me.filby.neptune.runescript.compiler.configuration.command.DynamicCommandHandler
import me.filby.neptune.runescript.compiler.configuration.command.TypeCheckingContext
import me.filby.neptune.runescript.compiler.type
import me.filby.neptune.runescript.compiler.type.MetaType
import me.filby.neptune.runescript.compiler.type.PrimitiveType
import me.filby.neptune.runescript.compiler.type.TupleType
import me.filby.neptune.runescript.compiler.type.Type

class MoveCheckCommandHandler(timerType: Type) : DynamicCommandHandler {
    private val triggerType = timerType as MetaType.Script

    override fun TypeCheckingContext.typeCheck() {
        checkArgument(0, triggerType) // script
        checkArgument(1, PrimitiveType.INT) // duration

        checkArgumentTypes(TupleType(triggerType, PrimitiveType.INT))
        expression.type = MetaType.Unit
    }
}
