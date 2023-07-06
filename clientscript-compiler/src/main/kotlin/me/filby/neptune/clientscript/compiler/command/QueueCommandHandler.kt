package me.filby.neptune.clientscript.compiler.command

import me.filby.neptune.runescript.compiler.codegen.Opcode
import me.filby.neptune.runescript.compiler.configuration.command.CodeGeneratorContext
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
        val queue = checkArgument(0, queueType)
        checkArgument(1, PrimitiveType.INT) // delay

        val queueExpressionType = queue?.type
        val expectedTypesList = mutableListOf(queueType, PrimitiveType.INT)
        if (
            queueExpressionType is MetaType.Script &&
            queueExpressionType.trigger == queueType.trigger &&
            queueExpressionType.parameterType != MetaType.Unit
        ) {
            expectedTypesList += TupleType.toList(queueExpressionType.parameterType)
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
