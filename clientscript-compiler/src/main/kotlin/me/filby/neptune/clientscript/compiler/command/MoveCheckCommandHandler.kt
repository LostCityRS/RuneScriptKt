package me.filby.neptune.clientscript.compiler.command

import me.filby.neptune.runescript.compiler.configuration.command.DynamicCommandHandler
import me.filby.neptune.runescript.compiler.configuration.command.TypeCheckingContext
import me.filby.neptune.runescript.compiler.type
import me.filby.neptune.runescript.compiler.type.MetaType
import me.filby.neptune.runescript.compiler.type.Type

class MoveCheckCommandHandler(timerType: Type) : DynamicCommandHandler {
    private val triggerType = timerType as MetaType.Script

    override fun TypeCheckingContext.typeCheck() {
        checkArgument(0, triggerType) // script

        checkArgumentTypes(triggerType)
        expression.type = MetaType.Unit
    }
}
