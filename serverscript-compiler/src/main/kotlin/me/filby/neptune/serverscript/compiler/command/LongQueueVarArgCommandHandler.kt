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

class LongQueueVarArgCommandHandler(queueType: Type) : DynamicCommandHandler {
    private val queueType = queueType as MetaType.Script

    override fun TypeCheckingContext.typeCheck() {
        val queue = checkArgument(0, queueType) // script to queue
        checkArgument(1, PrimitiveType.INT) // delay before running script
        checkArgument(2, PrimitiveType.INT) // action to perform if logout succeeds mid-queue

        val baseTypesList = mutableListOf(queueType, PrimitiveType.INT, PrimitiveType.INT)
        var varArgTypesList = emptyList<Type>()

        val queueExpressionType = queue?.type
        if (
            queueExpressionType is MetaType.Script &&
            queueExpressionType.trigger == queueType.trigger &&
            queueExpressionType.parameterType != MetaType.Unit
        ) {
            varArgTypesList += TupleType.toList(queueExpressionType.parameterType)
        }

        checkArgumentTypes(TupleType.fromList(baseTypesList), true, false)
        checkArgumentTypes(TupleType.fromList(varArgTypesList), true, true)
        expression.type = MetaType.Unit
    }

    override fun CodeGeneratorContext.generateCode() {
        val arguments = expression.arguments
        arguments.visit()

        val arguments2 = expression.arguments2
        arguments2.visit()

        if (arguments2.size > 0) {
            val shortTypes = arguments2
                .mapNotNull { it.type.code }
                .joinToString("")
            instruction(Opcode.PushConstantString, shortTypes)
        } else {
            instruction(Opcode.PushConstantString, "")
        }

        command()
    }
}
