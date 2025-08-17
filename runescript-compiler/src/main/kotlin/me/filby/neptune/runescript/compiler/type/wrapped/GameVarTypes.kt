package me.filby.neptune.runescript.compiler.type.wrapped

import me.filby.neptune.runescript.compiler.type.BaseVarType
import me.filby.neptune.runescript.compiler.type.Type

// base game variable type
public sealed interface GameVarType : WrappedType {
    override val code: Char?
        get() = null

    override val baseType: BaseVarType?
        get() = null

    override val defaultValue: Any?
        get() = null
}

// implementations
public data class VarPlayerType(override val inner: Type) : GameVarType {
    override val representation: String = "varp<${inner.representation}>"
}

public data class VarBitType(override val inner: Type) : GameVarType {
    override val representation: String = "varbit<${inner.representation}>"
}

public data class VarNpcType(override val inner: Type) : GameVarType {
    override val representation: String = "varn<${inner.representation}>"
}

public data class VarSharedType(override val inner: Type) : GameVarType {
    override val representation: String = "vars<${inner.representation}>"
}
