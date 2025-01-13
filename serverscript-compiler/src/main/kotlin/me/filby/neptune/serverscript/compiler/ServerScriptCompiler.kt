package me.filby.neptune.serverscript.compiler

import me.filby.neptune.runescript.compiler.ScriptCompiler
import me.filby.neptune.runescript.compiler.pointer.PointerHolder
import me.filby.neptune.runescript.compiler.type.MetaType
import me.filby.neptune.runescript.compiler.type.Type
import me.filby.neptune.runescript.compiler.type.wrapped.VarNpcType
import me.filby.neptune.runescript.compiler.type.wrapped.VarPlayerType
import me.filby.neptune.runescript.compiler.type.wrapped.VarSharedType
import me.filby.neptune.runescript.compiler.writer.ScriptWriter
import me.filby.neptune.serverscript.compiler.command.DbFindCommandHandler
import me.filby.neptune.serverscript.compiler.command.DbGetFieldCommandHandler
import me.filby.neptune.serverscript.compiler.command.EnumCommandHandler
import me.filby.neptune.serverscript.compiler.command.LongQueueCommandHandler
import me.filby.neptune.serverscript.compiler.command.ParamCommandHandler
import me.filby.neptune.serverscript.compiler.command.QueueCommandHandler
import me.filby.neptune.serverscript.compiler.command.TimerCommandHandler
import me.filby.neptune.serverscript.compiler.command.debug.DumpCommandHandler
import me.filby.neptune.serverscript.compiler.command.debug.ScriptCommandHandler
import me.filby.neptune.serverscript.compiler.trigger.ServerTriggerType
import me.filby.neptune.serverscript.compiler.type.DbColumnType
import me.filby.neptune.serverscript.compiler.type.ParamType
import me.filby.neptune.serverscript.compiler.type.ScriptVarType
import java.nio.file.Path
import kotlin.io.path.exists

class ServerScriptCompiler(
    sourcePaths: List<Path>,
    excludePaths: List<Path>,
    scriptWriter: ScriptWriter,
    commandPointers: Map<String, PointerHolder>,
    private val symbolPaths: List<Path>,
    private val mapper: SymbolMapper,
) : ScriptCompiler(sourcePaths, excludePaths, scriptWriter, commandPointers) {
    fun setup() {
        triggers.registerAll<ServerTriggerType>()
        types.registerAll<ScriptVarType>()
        types.changeOptions("long") {
            allowDeclaration = false
        }

        types.register("proc", MetaType.Script(ServerTriggerType.PROC, MetaType.Unit, MetaType.Unit))
        types.register("label", MetaType.Script(ServerTriggerType.LABEL, MetaType.Unit, MetaType.Nothing))

        // allow assignment of namedobj to obj
        types.addTypeChecker { left, right -> left == ScriptVarType.OBJ && right == ScriptVarType.NAMEDOBJ }

        // todo: macros
        addSymConstantLoaders()

        types.register("walktrigger", MetaType.Script(ServerTriggerType.WALKTRIGGER, MetaType.Any, MetaType.Nothing))
        types.register(
            "ai_walktrigger",
            MetaType.Script(ServerTriggerType.AI_WALKTRIGGER, MetaType.Any, MetaType.Nothing)
        )

        types.register("queue", MetaType.Script(ServerTriggerType.QUEUE, MetaType.Any, MetaType.Nothing))
        addDynamicCommandHandler("queue", QueueCommandHandler(types.find("queue")))
        addDynamicCommandHandler(".queue", QueueCommandHandler(types.find("queue")))
        addDynamicCommandHandler("longqueue", LongQueueCommandHandler(types.find("queue")))
        addDynamicCommandHandler(".longqueue", LongQueueCommandHandler(types.find("queue")))

        types.register("timer", MetaType.Script(ServerTriggerType.TIMER, MetaType.Any, MetaType.Nothing))
        addDynamicCommandHandler("settimer", TimerCommandHandler(types.find("timer")))
        addDynamicCommandHandler(".settimer", TimerCommandHandler(types.find("timer")))

        addDynamicCommandHandler("lc_param", ParamCommandHandler(ScriptVarType.LOC))
        addDynamicCommandHandler("loc_param", ParamCommandHandler(null))
        addSymLoader("loc", ScriptVarType.LOC)

        addDynamicCommandHandler("nc_param", ParamCommandHandler(ScriptVarType.NPC))
        addDynamicCommandHandler("npc_param", ParamCommandHandler(null))
        addSymLoader("npc", ScriptVarType.NPC)

        addDynamicCommandHandler("oc_param", ParamCommandHandler(ScriptVarType.OBJ))
        addDynamicCommandHandler("obj_param", ParamCommandHandler(null))
        addSymLoader("obj", ScriptVarType.NAMEDOBJ)

        addSymLoader("component", ScriptVarType.COMPONENT)
        addSymLoader("interface", ScriptVarType.INTERFACE)
        addSymLoader("overlayinterface", ScriptVarType.OVERLAYINTERFACE)
        addSymLoader("fontmetrics", ScriptVarType.FONTMETRICS)

        addSymLoader("category", ScriptVarType.CATEGORY)
        addSymLoader("hunt", ScriptVarType.HUNT)
        addSymLoader("inv", ScriptVarType.INV)
        addSymLoader("idk", ScriptVarType.IDKIT)
        addSymLoader("mesanim", ScriptVarType.MESANIM)
        addSymLoader("param", ::ParamType)
        addSymLoader("seq", ScriptVarType.SEQ)
        addSymLoader("spotanim", ScriptVarType.SPOTANIM)

        types.register("varp", VarPlayerType(MetaType.Any))
        addProtectedSymLoader("varp", ::VarPlayerType)
        addSymLoader("varn", ::VarNpcType)
        addSymLoader("vars", ::VarSharedType)

        addSymLoader("stat", ScriptVarType.STAT)
        addSymLoader("locshape", ScriptVarType.LOC_SHAPE)
        addSymLoader("movespeed", ScriptVarType.MOVESPEED)
        addSymLoader("npc_mode", ScriptVarType.NPC_MODE)
        addSymLoader("npc_stat", ScriptVarType.NPC_STAT)

        addSymLoader("model", ScriptVarType.MODEL)
        addSymLoader("synth", ScriptVarType.SYNTH)

        // mid-late 2004
        addDynamicCommandHandler("weakqueue", QueueCommandHandler(types.find("queue")))
        addDynamicCommandHandler(".weakqueue", QueueCommandHandler(types.find("queue")))
        // todo: varbit type

        // late 2004
        addDynamicCommandHandler("strongqueue", QueueCommandHandler(types.find("queue")))
        addDynamicCommandHandler(".strongqueue", QueueCommandHandler(types.find("queue")))

        // 2005
        addDynamicCommandHandler("enum", EnumCommandHandler())
        addSymLoader("enum", ScriptVarType.ENUM)
        // todo: mes type

        // 2006
        // todo: runclientscript command handler

        // 2009
        addDynamicCommandHandler("struct_param", ParamCommandHandler(ScriptVarType.STRUCT))
        addSymLoader("struct", ScriptVarType.STRUCT)
        types.register("softtimer", MetaType.Script(ServerTriggerType.SOFTTIMER, MetaType.Any, MetaType.Nothing))
        addDynamicCommandHandler("softtimer", TimerCommandHandler(types.find("softtimer")))
        addDynamicCommandHandler(".softtimer", TimerCommandHandler(types.find("softtimer")))

        // 2013 rs / 2018 osrs
        types.register("dbcolumn", DbColumnType(MetaType.Any))
        addDynamicCommandHandler("db_find", DbFindCommandHandler(true))
        addDynamicCommandHandler("db_find_refine", DbFindCommandHandler(true))
        addDynamicCommandHandler("db_getfield", DbGetFieldCommandHandler())
        addSymLoader("dbcolumn") { DbColumnType(it) }
        addSymLoader("dbrow", ScriptVarType.DBROW)
        addSymLoader("dbtable", ScriptVarType.DBTABLE)

        // debugging
        addDynamicCommandHandler("dump", DumpCommandHandler())
        addDynamicCommandHandler("script", ScriptCommandHandler())
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
        }
    }
}
