package me.filby.neptune.runescript.compiler.semantics

import me.filby.neptune.runescript.ast.AstVisitor
import me.filby.neptune.runescript.ast.Node
import me.filby.neptune.runescript.ast.Parameter
import me.filby.neptune.runescript.ast.Script
import me.filby.neptune.runescript.ast.ScriptFile
import me.filby.neptune.runescript.ast.statement.BlockStatement
import me.filby.neptune.runescript.ast.statement.SwitchCase
import me.filby.neptune.runescript.ast.statement.SwitchStatement
import me.filby.neptune.runescript.compiler.diagnostics.Diagnostic
import me.filby.neptune.runescript.compiler.diagnostics.DiagnosticMessage
import me.filby.neptune.runescript.compiler.diagnostics.DiagnosticType
import me.filby.neptune.runescript.compiler.diagnostics.Diagnostics
import me.filby.neptune.runescript.compiler.parameterType
import me.filby.neptune.runescript.compiler.returnType
import me.filby.neptune.runescript.compiler.scope
import me.filby.neptune.runescript.compiler.subjectReference
import me.filby.neptune.runescript.compiler.symbol
import me.filby.neptune.runescript.compiler.symbol.BasicSymbol
import me.filby.neptune.runescript.compiler.symbol.LocalVariableSymbol
import me.filby.neptune.runescript.compiler.symbol.ScriptSymbol
import me.filby.neptune.runescript.compiler.symbol.SymbolTable
import me.filby.neptune.runescript.compiler.symbol.SymbolType
import me.filby.neptune.runescript.compiler.trigger.SubjectMode
import me.filby.neptune.runescript.compiler.trigger.TriggerManager
import me.filby.neptune.runescript.compiler.trigger.TriggerType
import me.filby.neptune.runescript.compiler.triggerType
import me.filby.neptune.runescript.compiler.type
import me.filby.neptune.runescript.compiler.type.MetaType
import me.filby.neptune.runescript.compiler.type.PrimitiveType
import me.filby.neptune.runescript.compiler.type.TupleType
import me.filby.neptune.runescript.compiler.type.Type
import me.filby.neptune.runescript.compiler.type.TypeManager

/**
 * An [AstVisitor] implementation that handles the following.
 *
 * - Script declarations
 * - Switch statement type declaration, which is used later on in [TypeChecking]
 * - Local variable declarations
 * - Constant references
 */
internal class PreTypeChecking(
    private val typeManager: TypeManager,
    private val triggerManager: TriggerManager,
    private val rootTable: SymbolTable,
    private val diagnostics: Diagnostics,
) : AstVisitor<Unit> {
    /**
     * A stack of symbol tables to use through the script file.
     */
    private val tables = ArrayDeque<SymbolTable>()

    /**
     * The current active symbol table.
     */
    private val table get() = tables.first()

    /**
     * A cached reference to a [Type] representing a `category`.
     */
    private val categoryType = typeManager.findOrNull("category")

    init {
        // init with a base table for the file
        tables.addFirst(rootTable.createSubTable())
    }

    /**
     * Wraps [block] with creating a new [SymbolTable], pushing it to [tables] and then popping it back
     * out after the block is run.
     */
    private inline fun createScopedTable(crossinline block: () -> Unit) {
        tables.addFirst(table.createSubTable())
        block()
        tables.removeFirst()
    }

    override fun visitScriptFile(scriptFile: ScriptFile) {
        for (script in scriptFile.scripts) {
            createScopedTable {
                script.accept(this)
            }
        }
    }

    override fun visitScript(script: Script) {
        val trigger = triggerManager.findOrNull(script.trigger.text)
        if (trigger == null) {
            script.trigger.reportError(DiagnosticMessage.SCRIPT_TRIGGER_INVALID, script.trigger.text)
        } else {
            script.triggerType = trigger
        }

        // verify subject matched what the trigger requires
        checkScriptSubject(trigger, script)

        // visit the parameters
        val parameters = script.parameters
        parameters?.visit()

        // specify the parameter types for easy lookup later
        script.parameterType = TupleType.fromList(parameters?.map { it.symbol.type })

        // verify parameters match what the trigger type allows
        checkScriptParameters(trigger, script, parameters)

        // convert return type tokens into actual Types and attach to the script node
        val returnTokens = script.returnTokens
        if (!returnTokens.isNullOrEmpty()) {
            val returns = mutableListOf<Type>()
            for (token in returnTokens) {
                val type = typeManager.findOrNull(token.text)
                if (type == null) {
                    token.reportError(DiagnosticMessage.GENERIC_INVALID_TYPE, token.text)
                }
                returns += type ?: MetaType.Error
            }
            script.returnType = TupleType.fromList(returns)
        } else {
            // default return based on trigger if the trigger was found
            // triggers that allow returns will default to `unit` instead of `nothing`.
            script.returnType = if (trigger == null) {
                MetaType.Error
            } else if (trigger.allowReturns) {
                MetaType.Unit
            } else {
                MetaType.Nothing
            }
        }

        // verify returns match what the trigger type allows
        checkScriptReturns(trigger, script)

        if (trigger != null) {
            // attempt to insert the script into the root table and error if failed to insert
            val scriptSymbol = ScriptSymbol.ServerScriptSymbol(
                trigger, script.name.text,
                script.parameterType, script.returnType
            )
            val inserted = rootTable.insert(SymbolType.ServerScript(trigger), scriptSymbol)
            if (!inserted) {
                script.reportError(DiagnosticMessage.SCRIPT_REDECLARATION, trigger.identifier, script.name.text)
            } else {
                // only set the symbol if it was actually inserted
                script.symbol = scriptSymbol
            }
        }

        // visit the code
        script.statements.visit()

        // set the root symbol table for the script
        script.scope = table
    }

    /**
     * Validates the subject of [script] is allowed following [SubjectMode] for the
     * [trigger].
     */
    private fun checkScriptSubject(trigger: TriggerType?, script: Script) {
        val mode = trigger?.subjectMode ?: return
        val subject = script.name.text

        // name mode allows anything as the subject
        if (mode == SubjectMode.Name) {
            return
        }

        // check for global subject
        if (subject == "_") {
            checkGlobalScriptSubject(trigger, script)
            return
        }

        // check for category reference subject
        if (subject.startsWith("_")) {
            checkCategoryScriptSubject(trigger, script, subject.substring(1))
            return
        }

        // check for reference subject
        checkTypeScriptSubject(trigger, script, subject)
    }

    /**
     * Verifies the trigger subject mode is allowed to be a global subject.
     */
    private fun checkGlobalScriptSubject(trigger: TriggerType, script: Script) {
        val mode = trigger.subjectMode

        // trigger only allows global
        if (mode == SubjectMode.None) {
            return
        }

        // subject references a type, verify it allows global subject
        if (mode is SubjectMode.Type) {
            if (!mode.global) {
                script.name.reportError(DiagnosticMessage.SCRIPT_SUBJECT_NO_GLOBAL, trigger.identifier)
            }
            return
        }
        error(mode)
    }

    /**
     * Verifies the trigger subject mode is allowed to be a category subject.
     */
    private fun checkCategoryScriptSubject(trigger: TriggerType, script: Script, subject: String) {
        val mode = trigger.subjectMode
        val categoryType = categoryType ?: error("'category' type not defined.")

        // trigger only allows global
        if (mode == SubjectMode.None) {
            script.name.reportError(DiagnosticMessage.SCRIPT_SUBJECT_ONLY_GLOBAL, trigger.identifier)
            return
        }

        // subject references a type, verify it allows category subject
        if (mode is SubjectMode.Type) {
            if (!mode.category) {
                script.name.reportError(DiagnosticMessage.SCRIPT_SUBJECT_NO_CAT, trigger.identifier)
                return
            }

            // attempt to resolve the subject to a category
            resolveSubjectSymbol(script, subject, categoryType)
            return
        }
        error(mode)
    }

    /**
     * Verifies the trigger subject is allowed to refer to a type, category, or global subject.
     */
    private fun checkTypeScriptSubject(trigger: TriggerType, script: Script, subject: String) {
        val mode = trigger.subjectMode

        // trigger only allows global
        if (mode == SubjectMode.None) {
            script.name.reportError(DiagnosticMessage.SCRIPT_SUBJECT_ONLY_GLOBAL, trigger.identifier)
            return
        }

        // subject references a type
        if (mode is SubjectMode.Type) {
            // attempt to resolve the subject to the specified type
            resolveSubjectSymbol(script, subject, mode.type)
            return
        }
        error(mode)
    }

    private fun tryParseMapZone(script: Script, coord: String): Int {
        // format: level_mx_mz
        val parts = coord.split("_")
        if (parts.size != 3) {
            script.name.reportError("mapzone subject must be of the form 'level_mx_mz'")
            return -1
        }

        val (level, mx, mz) = parts
        val levelInt = level.toInt()
        val mxInt = mx.toInt()
        val mzInt = mz.toInt()

        if (mxInt < 0 || mxInt > 255 ||
            mzInt < 0 || mzInt > 255
        ) {
            script.name.reportError("Invalid mapzone coord")
            return -1
        }

        if (levelInt != 0) {
            script.name.reportError("mapzone affects all levels, just specify 0")
            return -1
        }

        val x = mxInt shl 6
        val z = mzInt shl 6
        return (z and 0x3fff) or ((x and 0x3fff) shl 14)
    }

    private fun tryParseZone(script: Script, coord: String): Int {
        // format: level_mx_mz_lx_lz
        val parts = coord.split("_")
        if (parts.size != 5) {
            script.name.reportError("zone subject must be of the form 'level_mx_mz_lx_lz'")
            return -1
        }

        val (level, mx, mz, lx, lz) = coord.split("_")
        val levelInt = level.toInt()
        val mxInt = mx.toInt()
        val mzInt = mz.toInt()
        val lxInt = lx.toInt()
        val lzInt = lz.toInt()

        if (levelInt < 0 || levelInt > 3 ||
            mxInt < 0 || mxInt > 255 ||
            mzInt < 0 || mzInt > 255 ||
            lxInt < 0 || lxInt > 63 ||
            lzInt < 0 || lzInt > 63
        ) {
            script.name.reportError("Invalid zone coord")
            return -1
        }

        if (lxInt % 8 != 0 || lzInt % 8 != 0) {
            script.name.reportError("Local zone coord must be a multiple of 8")
            return -1
        }

        val x = ((mxInt shl 6) or lxInt) shr 3 shl 3
        val z = ((mzInt shl 6) or lzInt) shr 3 shl 3
        return (z and 0x3fff) or ((x and 0x3fff) shl 14) or ((levelInt and 0x3) shl 28)
    }

    /**
     * Attempts to find a reference to the subject of a script.
     */
    private fun resolveSubjectSymbol(script: Script, subject: String, type: Type) {
        if (type == PrimitiveType.MAPZONE) {
            val packed = tryParseMapZone(script, subject)
            script.subjectReference = BasicSymbol(packed.toString(), type, false)
            return
        }

        if (type == PrimitiveType.COORD) {
            val packed = tryParseZone(script, subject)
            script.subjectReference = BasicSymbol(packed.toString(), type, false)
            return
        }

        val symbol = rootTable.find(SymbolType.Basic(type), subject)
        if (symbol == null) {
            script.name.reportError(DiagnosticMessage.GENERIC_UNRESOLVED_SYMBOL, subject)
            return
        }

        script.subjectReference = symbol
    }

    /**
     * Verifies the [script]s parameter types are what is allowed by the [trigger].
     */
    private fun checkScriptParameters(trigger: TriggerType?, script: Script, parameters: List<Parameter>?) {
        val triggerParameterType = trigger?.parameters
        val scriptParameterType = script.parameterType
        if (trigger != null && !trigger.allowParameters && !parameters.isNullOrEmpty()) {
            parameters.first().reportError(DiagnosticMessage.SCRIPT_TRIGGER_NO_PARAMETERS, trigger.identifier)
        } else if (triggerParameterType != null && scriptParameterType != triggerParameterType) {
            val expectedParameterType = triggerParameterType.representation
            script.reportError(
                DiagnosticMessage.SCRIPT_TRIGGER_EXPECTED_PARAMETERS,
                script.trigger.text,
                expectedParameterType
            )
        }
    }

    /**
     * Verifies the [script] returns what is allowed by the [trigger].
     */
    private fun checkScriptReturns(trigger: TriggerType?, script: Script) {
        val triggerReturns = trigger?.returns
        val scriptReturns = script.returnType
        if (trigger != null && !trigger.allowReturns && scriptReturns != MetaType.Nothing) {
            script.reportError(DiagnosticMessage.SCRIPT_TRIGGER_NO_RETURNS, trigger.identifier)
        } else if (triggerReturns != null && scriptReturns != triggerReturns) {
            val expectedReturnTypes = triggerReturns.representation
            script.reportError(
                DiagnosticMessage.SCRIPT_TRIGGER_EXPECTED_RETURNS,
                script.trigger.text,
                expectedReturnTypes
            )
        }
    }

    override fun visitParameter(parameter: Parameter) {
        val name = parameter.name.text
        val typeText = parameter.typeToken.text
        val type = typeManager.findOrNull(typeText, allowArray = true)

        // type isn't valid, report the error
        if (type == null) {
            parameter.reportError(DiagnosticMessage.GENERIC_INVALID_TYPE, typeText)
        }

        // attempt to insert the local variable into the symbol table and display error if failed to insert
        val symbol = LocalVariableSymbol(name, type ?: MetaType.Error)
        val inserted = table.insert(SymbolType.LocalVariable, symbol)
        if (!inserted) {
            parameter.reportError(DiagnosticMessage.SCRIPT_LOCAL_REDECLARATION, name)
        }

        parameter.symbol = symbol
    }

    override fun visitBlockStatement(blockStatement: BlockStatement) {
        createScopedTable {
            // visit inner statements
            blockStatement.statements.visit()

            // set the symbol table for the block
            blockStatement.scope = table
        }
    }

    override fun visitSwitchStatement(switchStatement: SwitchStatement) {
        val typeName = switchStatement.typeToken.text.removePrefix("switch_")
        val type = typeManager.findOrNull(typeName)

        // notify invalid type
        if (type == null) {
            switchStatement.typeToken.reportError(DiagnosticMessage.GENERIC_INVALID_TYPE, typeName)
        } else if (!type.options.allowSwitch) {
            switchStatement.typeToken.reportError(DiagnosticMessage.SWITCH_INVALID_TYPE, type.representation)
        }

        // visit the condition to resolve any reference
        switchStatement.condition.accept(this)

        // visit the cases to resolve references in them
        switchStatement.cases.visit()

        // set the expected type of the switch cases
        switchStatement.type = type ?: MetaType.Error
    }

    override fun visitSwitchCase(switchCase: SwitchCase) {
        // visit the keys to set any types that can be set early
        switchCase.keys.visit()

        // create a new scope and visit the statements in it
        createScopedTable {
            // visit inner statements
            switchCase.statements.visit()

            // set the symbol table for the block
            switchCase.scope = table
        }
    }

    override fun visitNode(node: Node) {
        node.children.visit()
    }

    /**
     * Helper function to report a diagnostic with the type of [DiagnosticType.INFO].
     */
    private fun Node.reportInfo(message: String, vararg args: Any) {
        diagnostics.report(Diagnostic(DiagnosticType.INFO, this, message, *args))
    }

    /**
     * Helper function to report a diagnostic with the type of [DiagnosticType.WARNING].
     */
    private fun Node.reportWarning(message: String, vararg args: Any) {
        diagnostics.report(Diagnostic(DiagnosticType.WARNING, this, message, *args))
    }

    /**
     * Helper function to report a diagnostic with the type of [DiagnosticType.ERROR].
     */
    private fun Node.reportError(message: String, vararg args: Any) {
        diagnostics.report(Diagnostic(DiagnosticType.ERROR, this, message, *args))
    }

    /**
     * Calls [Node.accept] on all nodes in a list.
     */
    private fun List<Node>.visit() {
        for (n in this) {
            n.accept(this@PreTypeChecking)
        }
    }
}
