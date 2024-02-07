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
import me.filby.neptune.runescript.compiler.pointer.PointerHolder
import me.filby.neptune.runescript.compiler.type.MetaType
import me.filby.neptune.runescript.compiler.type.PrimitiveType
import me.filby.neptune.runescript.compiler.type.Type
import me.filby.neptune.runescript.compiler.type.wrapped.VarNpcType
import me.filby.neptune.runescript.compiler.type.wrapped.VarPlayerType
import me.filby.neptune.runescript.compiler.type.wrapped.VarSharedType
import me.filby.neptune.runescript.compiler.writer.ScriptWriter
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

class ClientScriptCompiler(
    sourcePaths: List<Path>,
    excludePaths: List<Path>,
    scriptWriter: ScriptWriter,
    commandPointers: Map<String, PointerHolder>,
    private val symbolPaths: List<Path>,
    private val mapper: SymbolMapper,
) : ScriptCompiler(sourcePaths, excludePaths, scriptWriter, commandPointers) {
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
        addSymConstantLoaders()

        addSymLoader("category", ScriptVarType.CATEGORY)
        addSymLoader("component", ScriptVarType.COMPONENT)
        addSymLoader("dbcolumn") { DbColumnType(it) }
        addSymLoader("dbrow", ScriptVarType.DBROW)
        addSymLoader("dbtable", ScriptVarType.DBTABLE)
        addSymLoader("enum", ScriptVarType.ENUM)
        addSymLoader("fontmetrics", ScriptVarType.FONTMETRICS)
        addSymLoader("hunt", ScriptVarType.HUNT)
        addSymLoader("interface", ScriptVarType.INTERFACE)
        addSymLoader("inv", ScriptVarType.INV)
        addSymLoader("idk", ScriptVarType.IDKIT)
        addSymLoader("loc", ScriptVarType.LOC)
        addSymLoader("locshape", ScriptVarType.LOC_SHAPE)
        addSymLoader("mesanim", ScriptVarType.MESANIM)
        addSymLoader("model", ScriptVarType.MODEL)
        addSymLoader("movespeed", ScriptVarType.MOVESPEED)
        addSymLoader("npc", ScriptVarType.NPC)
        addSymLoader("npc_mode", ScriptVarType.NPC_MODE)
        addSymLoader("npc_stat", ScriptVarType.NPC_STAT)
        addSymLoader("obj", ScriptVarType.NAMEDOBJ)
        addSymLoader("overlayinterface", ScriptVarType.OVERLAYINTERFACE)
        addSymLoader("param", ::ParamType)
        addSymLoader("seq", ScriptVarType.SEQ)
        addSymLoader("spotanim", ScriptVarType.SPOTANIM)
        addSymLoader("stat", ScriptVarType.STAT)
        addSymLoader("struct", ScriptVarType.STRUCT)
        addSymLoader("synth", ScriptVarType.SYNTH)
        addSymLoader("varn", ::VarNpcType)
        addProtectedSymLoader("varp", ::VarPlayerType)
        addSymLoader("vars", ::VarSharedType)
    }

    /**
     * Looks for `constant.sym` and all `sym` files in `/constant` and registers them
     * with a [ConstantLoader].
     */
    private fun addSymConstantLoaders() {
        for (symbolPath in symbolPaths) {
            // look for {symbol_path}/constant.sym
            val constantsFile = symbolPath.resolve("constant.sym")
            if (constantsFile.exists()) {
                addSymbolLoader(ConstantLoader(constantsFile))
            }

            // look for {symbol_path}/constant/**.sym
            val constantDir = symbolPath.resolve("constant")
            if (constantDir.exists() && constantDir.isDirectory()) {
                val files = constantDir
                    .toFile()
                    .walkTopDown()
                    .filter { it.isFile && it.extension == "sym" }
                for (file in files) {
                    addSymbolLoader(ConstantLoader(file.toPath()))
                }
            }
        }
    }

    /**
     * Helper for loading external symbols from `sym` files with a specific [type].
     */
    private fun addSymLoader(name: String, type: Type) {
        addSymLoader(name) { type }
    }

    /**
     * Helper for loading external symbols from `sym` files with subtypes.
     */
    private fun addSymLoader(name: String, typeSuppler: (subTypes: Type) -> Type) {
        for (symbolPath in symbolPaths) {
            // look for {symbol_path}/{name}.sym
            val typeFile = symbolPath.resolve("$name.sym")
            if (typeFile.exists()) {
                addSymbolLoader(TsvSymbolLoader(mapper, typeFile, typeSuppler))
            }

            // look for {symbol_path}/{name}/**.sym
            val typeDir = symbolPath.resolve(name)
            if (typeDir.exists() && typeDir.isDirectory()) {
                val files = typeDir
                    .toFile()
                    .walkTopDown()
                    .filter { it.isFile && it.extension == "sym" }
                for (file in files) {
                    addSymbolLoader(TsvSymbolLoader(mapper, file.toPath(), typeSuppler))
                }
            }
        }
    }

    /**
     * Helper for loading external symbols from `sym` files with a specific [type] protection flag.
     */
    private fun addProtectedSymLoader(name: String, type: Type) {
        addProtectedSymLoader(name) { type }
    }

    /**
     * Helper for loading external symbols from `sym` files with subtypes and protection flag.
     */
    private fun addProtectedSymLoader(name: String, typeSuppler: (subTypes: Type) -> Type) {
        for (symbolPath in symbolPaths) {
            // look for {symbol_path}/{name}.sym
            val typeFile = symbolPath.resolve("$name.sym")
            if (typeFile.exists()) {
                addSymbolLoader(TsvProtectedSymbolLoader(mapper, typeFile, typeSuppler))
            }

            // look for {symbol_path}/{name}/**.sym
            val typeDir = symbolPath.resolve(name)
            if (typeDir.exists() && typeDir.isDirectory()) {
                val files = typeDir
                    .toFile()
                    .walkTopDown()
                    .filter { it.isFile && it.extension == "sym" }
                for (file in files) {
                    addSymbolLoader(TsvProtectedSymbolLoader(mapper, file.toPath(), typeSuppler))
                }
            }
        }
    }
}
