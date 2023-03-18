package me.filby.neptune.runescript.compiler.trigger

import me.filby.neptune.runescript.compiler.type.Type

enum class TestTriggerType(
    override val id: Int,
    override val subjectType: Type? = null,
    override val allowParameters: Boolean = false,
    override val parameters: Type? = null,
    override val allowReturns: Boolean = false,
    override val returns: Type? = null,
) : TriggerType {
    PROC(0, allowParameters = true, allowReturns = true),
    ;

    override val identifier: String get() = name.lowercase()
}