package me.filby.neptune.runescript.compiler.trigger

import me.filby.neptune.runescript.compiler.type.Type

public object CommandTrigger : TriggerType {
    override val id: Int = -1
    override val identifier: String = "command"
    override val subjectType: Type? = null
    override val allowParameters: Boolean = true
    override val parameters: Type? = null
    override val allowReturns: Boolean = true
    override val returns: Type? = null
}