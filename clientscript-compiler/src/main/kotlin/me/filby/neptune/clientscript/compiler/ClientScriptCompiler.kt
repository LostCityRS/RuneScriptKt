package me.filby.neptune.clientscript.compiler

import me.filby.neptune.clientscript.compiler.command.DbFindCommandHandler
import me.filby.neptune.clientscript.compiler.command.DbGetFieldCommandHandler
import me.filby.neptune.clientscript.compiler.command.EnumCommandHandler
import me.filby.neptune.clientscript.compiler.command.MoveCheckCommandHandler
import me.filby.neptune.clientscript.compiler.command.ParamCommandHandler
import me.filby.neptune.clientscript.compiler.command.QueueCommandHandler
import me.filby.neptune.clientscript.compiler.command.TimerCommandHandler
import me.filby.neptune.clientscript.compiler.trigger.ClientTriggerType
import me.filby.neptune.clientscript.compiler.type.DbColumnType
import me.filby.neptune.clientscript.compiler.type.ParamType
import me.filby.neptune.clientscript.compiler.type.ScriptVarType
import me.filby.neptune.runescript.compiler.ScriptCompiler
import me.filby.neptune.runescript.compiler.type.MetaType
import me.filby.neptune.runescript.compiler.type.PrimitiveType
import me.filby.neptune.runescript.compiler.type.Type
import me.filby.neptune.runescript.compiler.type.wrapped.VarNpcType
import me.filby.neptune.runescript.compiler.type.wrapped.VarPlayerType
import me.filby.neptune.runescript.compiler.type.wrapped.VarSharedType
import me.filby.neptune.runescript.compiler.writer.ScriptWriter
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

class ClientScriptCompiler(
    sourcePath: Path,
    scriptWriter: ScriptWriter,
    private val mapper: SymbolMapper,
) : ScriptCompiler(sourcePath, scriptWriter) {
    fun setup() {
        triggers.registerAll<ClientTriggerType>()

        // register types
        types.registerAll<ScriptVarType>()
        types.changeOptions("long") {
            allowDeclaration = false
        }

        // special types for commands
        types.register("dbcolumn", DbColumnType(MetaType.Any))
        types.register("varp", VarPlayerType(MetaType.Any))
        types.register("proc", MetaType.Script(ClientTriggerType.PROC, MetaType.Unit, MetaType.Unit))
        types.register("label", MetaType.Script(ClientTriggerType.LABEL, MetaType.Unit, MetaType.Nothing))
        types.register("queue", MetaType.Script(ClientTriggerType.QUEUE, MetaType.Any, MetaType.Nothing))
        types.register("timer", MetaType.Script(ClientTriggerType.TIMER, MetaType.Any, MetaType.Nothing))
        types.register("softtimer", MetaType.Script(ClientTriggerType.SOFTTIMER, MetaType.Any, MetaType.Nothing))
        types.register("movecheck", MetaType.Script(ClientTriggerType.MOVECHECK, MetaType.Unit, PrimitiveType.BOOLEAN))
        types.register(
            "ai_movecheck",
            MetaType.Script(ClientTriggerType.AI_MOVECHECK, MetaType.Unit, PrimitiveType.BOOLEAN)
        )

        // allow assignment of namedobj to obj
        types.addTypeChecker { left, right -> left == ScriptVarType.OBJ && right == ScriptVarType.NAMEDOBJ }

        // register the dynamic command handlers
        addDynamicCommandHandler("db_find", DbFindCommandHandler(true))
        addDynamicCommandHandler("db_find_refine", DbFindCommandHandler(true))
        addDynamicCommandHandler("db_getfield", DbGetFieldCommandHandler())
        addDynamicCommandHandler("enum", EnumCommandHandler())
        addDynamicCommandHandler("lc_param", ParamCommandHandler(ScriptVarType.LOC))
        addDynamicCommandHandler("loc_param", ParamCommandHandler(null))
        addDynamicCommandHandler("longqueue", QueueCommandHandler(types.find("queue")))
        addDynamicCommandHandler("nc_param", ParamCommandHandler(ScriptVarType.NPC))
        addDynamicCommandHandler("npc_param", ParamCommandHandler(null))
        addDynamicCommandHandler("npc_setmovecheck", MoveCheckCommandHandler(types.find("ai_movecheck")))
        addDynamicCommandHandler("obj_param", ParamCommandHandler(null))
        addDynamicCommandHandler("oc_param", ParamCommandHandler(ScriptVarType.OBJ))
        addDynamicCommandHandler("queue", QueueCommandHandler(types.find("queue")))
        addDynamicCommandHandler("setmovecheck", MoveCheckCommandHandler(types.find("movecheck")))
        addDynamicCommandHandler("settimer", TimerCommandHandler(types.find("timer")))
        addDynamicCommandHandler("softtimer", TimerCommandHandler(types.find("softtimer")))
        addDynamicCommandHandler("strongqueue", QueueCommandHandler(types.find("queue")))
        addDynamicCommandHandler("struct_param", ParamCommandHandler(ScriptVarType.STRUCT))
        addDynamicCommandHandler("weakqueue", QueueCommandHandler(types.find("queue")))

        // symbol loaders
        addTsvConstantLoaders()

        addTsvLoader("category", ScriptVarType.CATEGORY)
        addTsvLoader("component", ScriptVarType.COMPONENT)
        addTsvLoader("dbcolumn") { DbColumnType(it) }
        addTsvLoader("dbrow", ScriptVarType.DBROW)
        addTsvLoader("dbtable", ScriptVarType.DBTABLE)
        addTsvLoader("enum", ScriptVarType.ENUM)
        addTsvLoader("fontmetrics", ScriptVarType.FONTMETRICS)
        addTsvLoader("hunt", ScriptVarType.HUNT)
        addTsvLoader("interface", ScriptVarType.INTERFACE)
        addTsvLoader("inv", ScriptVarType.INV)
        addTsvLoader("loc", ScriptVarType.LOC)
        addTsvLoader("locshape", ScriptVarType.LOC_SHAPE)
        addTsvLoader("mesanim", ScriptVarType.MESANIM)
        addTsvLoader("model", ScriptVarType.MODEL)
        addTsvLoader("movespeed", ScriptVarType.MOVESPEED)
        addTsvLoader("npc", ScriptVarType.NPC)
        addTsvLoader("npc_mode", ScriptVarType.NPC_MODE)
        addTsvLoader("npc_stat", ScriptVarType.NPC_STAT)
        addTsvLoader("obj", ScriptVarType.NAMEDOBJ)
        addTsvLoader("param") { ParamType(it) }
        addTsvLoader("seq", ScriptVarType.SEQ)
        addTsvLoader("spotanim", ScriptVarType.SPOTANIM)
        addTsvLoader("stat", ScriptVarType.STAT)
        addTsvLoader("struct", ScriptVarType.STRUCT)
        addTsvLoader("synth", ScriptVarType.SYNTH)
        addTsvLoader("varn") { VarNpcType(it) }
        addTsvLoader("varp") { VarPlayerType(it) }
        addTsvLoader("vars") { VarSharedType(it) }
    }

    /**
     * Looks for `constant.tsv` and all `tsv` files in `/constant` and registers them
     * with a [ConstantLoader].
     */
    private fun addTsvConstantLoaders() {
        // look for {symbol_path}/constant.tsv
        val constantsFile = SYMBOLS_PATH.resolve("constant.tsv")
        if (constantsFile.exists()) {
            addSymbolLoader(ConstantLoader(constantsFile))
        }

        // look for {symbol_path}/constant/**.tsv
        val constantDir = SYMBOLS_PATH.resolve("constant")
        if (constantDir.exists() && constantDir.isDirectory()) {
            val files = constantDir
                .toFile()
                .walkTopDown()
                .filter { it.isFile && it.extension == "tsv" }
            for (file in files) {
                addSymbolLoader(ConstantLoader(file.toPath()))
            }
        }
    }

    /**
     * Helper for loading external symbols from `tsv` files with a specific [type].
     */
    private fun addTsvLoader(name: String, type: Type) {
        addTsvLoader(name) { type }
    }

    /**
     * Helper for loading external symbols from `tsv` files with subtypes.
     */
    private fun addTsvLoader(name: String, typeSuppler: (subTypes: Type) -> Type) {
        // look for {symbol_path}/{name}.tsv
        val typeFile = SYMBOLS_PATH.resolve("$name.tsv")
        if (typeFile.exists()) {
            addSymbolLoader(TsvSymbolLoader(mapper, typeFile, typeSuppler))
        }

        // look for {symbol_path}/{name}/**.tsv
        val typeDir = SYMBOLS_PATH.resolve(name)
        if (typeDir.exists() && typeDir.isDirectory()) {
            val files = typeDir
                .toFile()
                .walkTopDown()
                .filter { it.isFile && it.extension == "tsv" }
            for (file in files) {
                addSymbolLoader(TsvSymbolLoader(mapper, file.toPath(), typeSuppler))
            }
        }
    }

    private companion object {
        val SYMBOLS_PATH = Path("symbols")
    }
}
