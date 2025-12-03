package me.filby.neptune.serverscript.compiler.command

import me.filby.neptune.runescript.compiler.configuration.command.DynamicCommandHandler
import me.filby.neptune.runescript.compiler.configuration.command.TypeCheckingContext
import me.filby.neptune.runescript.compiler.type
import me.filby.neptune.runescript.compiler.type.MetaType
import me.filby.neptune.runescript.compiler.type.PrimitiveType
import me.filby.neptune.runescript.compiler.type.TupleType
import me.filby.neptune.runescript.compiler.type.Type

class QueueCommandHandler(queueType: Type) : DynamicCommandHandler {
    private val queueType = queueType as MetaType.Script

    override fun TypeCheckingContext.typeCheck() {
        checkArgument(0, queueType) // script to queue
        checkArgument(1, PrimitiveType.INT) // delay before running script
        checkArgument(2, PrimitiveType.INT) // int arg to pass to script

        // todo: (type safety) make sure queue script only expects up to 1 int arg (parameterType)

        val expectedTypesList = mutableListOf(queueType, PrimitiveType.INT, PrimitiveType.INT)
        checkArgumentTypes(TupleType.fromList(expectedTypesList))
        expression.type = MetaType.Unit
    }
}
