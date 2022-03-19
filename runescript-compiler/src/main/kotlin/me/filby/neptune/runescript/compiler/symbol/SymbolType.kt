package me.filby.neptune.runescript.compiler.symbol

import me.filby.neptune.runescript.compiler.trigger.ClientTriggerType
import me.filby.neptune.runescript.compiler.type.PrimitiveType

public sealed class SymbolType<T : Symbol> {
    public object ServerScript : SymbolType<ServerScriptSymbol>()
    public data class ClientScript(public val type: ClientTriggerType) : SymbolType<ClientScriptSymbol>()
    public data class Config(public val type: PrimitiveType) : SymbolType<ConfigSymbol>()

    override fun toString(): String = this::class.java.simpleName
}
