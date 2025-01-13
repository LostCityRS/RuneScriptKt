package me.filby.neptune.serverscript.compiler.command

import me.filby.neptune.runescript.compiler.codegen.Opcode
import me.filby.neptune.runescript.compiler.configuration.command.CodeGeneratorContext
import me.filby.neptune.runescript.compiler.configuration.command.DynamicCommandHandler
import me.filby.neptune.runescript.compiler.configuration.command.TypeCheckingContext
import me.filby.neptune.runescript.compiler.type
import me.filby.neptune.runescript.compiler.type.MetaType
import me.filby.neptune.runescript.compiler.type.PrimitiveType
import me.filby.neptune.runescript.compiler.type.TupleType
import me.filby.neptune.runescript.compiler.type.Type

class TimerCommandHandler(timerType: Type) : DynamicCommandHandler {
    private val timerType = timerType as MetaType.Script

    override fun TypeCheckingContext.typeCheck() {
        val timer = checkArgument(0, timerType)
        checkArgument(1, PrimitiveType.INT) // interval

        val timerExpressionType = timer?.type
        val expectedTypesList = mutableListOf(timerType, PrimitiveType.INT)
        if (
            timerExpressionType is MetaType.Script &&
            timerExpressionType.trigger == timerType.trigger &&
            timerExpressionType.parameterType != MetaType.Unit
        ) {
            expectedTypesList += TupleType.toList(timerExpressionType.parameterType)
        }

        checkArgumentTypes(TupleType.fromList(expectedTypesList))
        expression.type = MetaType.Unit
    }

    override fun CodeGeneratorContext.generateCode() {
        val arguments = expression.arguments

        // visit all arguments
        arguments.visit()

        // if there are more than 2 arguments that means the queue expects additional parameters,
        // so we must build a string of the argument types to push
        if (arguments.size > 2) {
            val shortTypes = arguments
                .subList(2, arguments.size)
                .mapNotNull { it.type.code }
                .joinToString("")
            instruction(Opcode.PushConstantString, shortTypes)
        } else {
            instruction(Opcode.PushConstantString, "")
        }

        command()
    }
}
