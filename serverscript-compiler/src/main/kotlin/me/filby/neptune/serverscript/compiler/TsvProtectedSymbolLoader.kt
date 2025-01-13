package me.filby.neptune.serverscript.compiler

import me.filby.neptune.runescript.compiler.ScriptCompiler
import me.filby.neptune.runescript.compiler.configuration.SymbolLoader
import me.filby.neptune.runescript.compiler.symbol.SymbolTable
import me.filby.neptune.runescript.compiler.type.MetaType
import me.filby.neptune.runescript.compiler.type.TupleType
import me.filby.neptune.runescript.compiler.type.Type
import java.nio.file.Path
import kotlin.io.path.useLines

class TsvProtectedSymbolLoader(
    private val mapper: SymbolMapper,
    private val path: Path,
    private val typeSupplier: (subTypes: Type) -> Type,
) : SymbolLoader {
    override fun SymbolTable.load(compiler: ScriptCompiler) {
        path.useLines { lines ->
            for (line in lines) {
                val split = line.split('\t')
                if (split.size < 2) {
                    continue
                }

                val id = split[0].toInt()
                val name = split[1]
                val subTypes = if (split.size >= 3 && split[2] != "none") {
                    val typeSplit = split[2].split(',')
                    val types = typeSplit.map { typeName -> compiler.types.find(typeName) }
                    TupleType.fromList(types)
                } else {
                    MetaType.Unit
                }
                val protected = if (split.size >= 4) {
                    split[3].toBoolean()
                } else {
                    false
                }
                val type = typeSupplier(subTypes)

                val symbol = addBasic(type, name, protected)
                mapper.putSymbol(id, symbol)
            }
        }
    }
}
