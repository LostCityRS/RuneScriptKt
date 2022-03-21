package me.filby.neptune.runescript.compiler.semantics

import me.filby.neptune.runescript.ast.AstVisitor
import me.filby.neptune.runescript.ast.Node
import me.filby.neptune.runescript.ast.Parameter
import me.filby.neptune.runescript.ast.Script
import me.filby.neptune.runescript.ast.ScriptFile
import me.filby.neptune.runescript.compiler.diagnostics.Diagnostic
import me.filby.neptune.runescript.compiler.diagnostics.DiagnosticMessage
import me.filby.neptune.runescript.compiler.diagnostics.DiagnosticType
import me.filby.neptune.runescript.compiler.diagnostics.Diagnostics
import me.filby.neptune.runescript.compiler.symbol.ClientScriptSymbol
import me.filby.neptune.runescript.compiler.symbol.LocalVariableSymbol
import me.filby.neptune.runescript.compiler.symbol.SymbolTable
import me.filby.neptune.runescript.compiler.symbol.SymbolType
import me.filby.neptune.runescript.compiler.trigger.ClientTriggerType
import me.filby.neptune.runescript.compiler.type.ArrayType
import me.filby.neptune.runescript.compiler.type.PrimitiveType
import me.filby.neptune.runescript.compiler.type.TupleType
import me.filby.neptune.runescript.compiler.type.Type

/**
 * The script parameter type(s) if it returns any.
 */
private var Script.parameterType by Node.attributeOrNull<Type>("parameterType")

/**
 * The script return type(s) if it returns any.
 */
private var Script.returnType by Node.attributeOrNull<Type>("returnType")

/**
 * The defined type of the parameter.
 */
private var Parameter.type by Node.attribute<Type>("type")

/**
 * The scope defined for the node. This should only ever be set for node types that
 * would create a new scope.
 */
private var Node.scope by Node.attribute<SymbolTable>("scope")

// TODO rename class
internal class SemanticChecker(
    private val rootTable: SymbolTable,
    private val diagnostics: Diagnostics
) : AstVisitor<Unit> {
    /**
     * A stack of symbol tables to use through the script file.
     */
    private val tables = ArrayDeque<SymbolTable>()

    /**
     * The current active symbol table.
     */
    private val table get() = tables.first()

    init {
        // init with a base table for the file
        tables.addFirst(SymbolTable())
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
                script.scope = table
                script.accept(this)
            }
        }
    }

    override fun visitScript(script: Script) {
        val trigger = ClientTriggerType.lookup(script.trigger.text)
        if (trigger == null) {
            script.trigger.reportError(DiagnosticMessage.SCRIPT_TRIGGER_INVALID, script.trigger.text)
        }

        if (trigger != null) {
            // attempt to insert the script into the root table and error if failed to insert
            val scriptSymbol = ClientScriptSymbol(script.name.text)
            val inserted = rootTable.insert(SymbolType.ClientScript(trigger), scriptSymbol)
            if (!inserted) {
                // TODO somehow report original declaration location?
                script.reportError(DiagnosticMessage.SCRIPT_REDECLARATION, trigger.identifier, script.name.text)
            }
        }

        // TODO check subject if it's meant to refer to a specific thing

        // visit the parameters
        val parameters = script.parameters
        parameters?.visit()

        // specify the parameter types for easy lookup later
        script.parameterType = TupleType.fromList(parameters?.map { it.type })

        // verify parameters match what the trigger type allows
        checkScriptParameters(trigger, script, parameters)

        // convert return type tokens into actual Types and attach to the script node
        val returnTokens = script.returnTokens
        if (!returnTokens.isNullOrEmpty()) {
            val returns = mutableListOf<Type>()
            for (token in returnTokens) {
                val type = lookupType(token.text) ?: PrimitiveType.UNDEFINED
                if (type == PrimitiveType.UNDEFINED) {
                    token.reportError(DiagnosticMessage.GENERIC_INVALID_TYPE, token.text)
                }
                returns += type
            }
            script.returnType = TupleType.fromList(returns)
        }

        // verify returns match what the trigger type allows
        checkScriptReturns(trigger, script)

        // visit the code
        script.statements.visit()
    }

    /**
     * Verifies the [script]s parameter types are what is allowed by the [trigger].
     */
    private fun checkScriptParameters(trigger: ClientTriggerType?, script: Script, parameters: List<Parameter>?) {
        val triggerParameterType = trigger?.parameters
        val scriptParameterType = script.parameterType
        if (trigger != null && !trigger.allowParameters && !parameters.isNullOrEmpty()) {
            parameters.first().reportError(DiagnosticMessage.SCRIPT_TRIGGER_NO_PARAMETERS, trigger.identifier)
        } else if (triggerParameterType != null && scriptParameterType != triggerParameterType) {
            // TODO be smarter on where to place the error location to the first type mismatch?
            val expectedParameterType = triggerParameterType.representation
            val currentParameterType = scriptParameterType?.representation ?: ""
            script.reportError(
                DiagnosticMessage.SCRIPT_TRIGGER_EXPECTED_PARAMETERS,
                script.trigger.text,
                expectedParameterType,
                currentParameterType
            )
        }
    }

    /**
     * Verifies the [script] returns what is allowed by the [trigger].
     */
    private fun checkScriptReturns(trigger: ClientTriggerType?, script: Script) {
        val triggerReturns = trigger?.returns
        val scriptReturns = script.returnType
        if (trigger != null && !trigger.allowReturns && scriptReturns != null) {
            script.reportError(DiagnosticMessage.SCRIPT_TRIGGER_NO_RETURNS, trigger.identifier)
        } else if (triggerReturns != null && scriptReturns != triggerReturns) {
            // TODO be smarter on where to place the error location to the first type mismatch?
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
        val type = lookupType(typeText, allowArray = true) ?: PrimitiveType.UNDEFINED

        // type isn't valid, report the error
        if (type == PrimitiveType.UNDEFINED) {
            parameter.reportError(DiagnosticMessage.GENERIC_INVALID_TYPE, typeText)
        }

        // attempt to insert the local variable into the symbol table and display error if failed to insert
        val inserted = table.insert(SymbolType.LocalVariable, LocalVariableSymbol(name, type))
        if (!inserted) {
            parameter.reportError(DiagnosticMessage.SCRIPT_LOCAL_REDECLARATION, name)
        }

        parameter.type = type
    }

    override fun visitNode(node: Node) {
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
            // TODO check return type to prevent continuing when we shouldn't, possibly configurable with params
            n.accept(this@SemanticChecker)
        }
    }

    /**
     * Lookup a type by its name. When [allowArray], types ending with `array` will return [ArrayType].
     */
    private fun lookupType(name: String, allowArray: Boolean = false): Type? {
        if (allowArray && name.endsWith("array")) {
            // substring before last "array" to prevent requesting intarrayarray (or deeper)
            val baseType = name.substringBeforeLast("array")
            val type = lookupType(baseType) ?: return null
            return ArrayType(type)
        }
        // TODO: lookup type from a type supplier
        // TODO: support for not allowing specific types (e.g. NULL)
        return PrimitiveType.lookup(name)
    }
}