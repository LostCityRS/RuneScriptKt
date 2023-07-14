package me.filby.neptune.clientscript.compiler.trigger

import me.filby.neptune.clientscript.compiler.type.ScriptVarType
import me.filby.neptune.runescript.compiler.trigger.SubjectMode
import me.filby.neptune.runescript.compiler.trigger.TriggerType
import me.filby.neptune.runescript.compiler.type.Type

/**
 * An enumeration of valid trigger types for use in ClientScript.
 */
enum class ClientTriggerType(
    override val id: Int,
    override val subjectMode: SubjectMode = SubjectMode.Name,
    override val allowParameters: Boolean = false,
    override val parameters: Type? = null,
    override val allowReturns: Boolean = false,
    override val returns: Type? = null,
) : TriggerType {
    PROC(0, allowParameters = true, allowReturns = true),
    LABEL(1, allowParameters = true),

    APNPC1(2, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    APNPC2(3, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    APNPC3(4, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    APNPC4(5, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    APNPC5(6, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    OPNPC1(7, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    OPNPC2(8, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    OPNPC3(9, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    OPNPC4(10, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    OPNPC5(11, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),

    AI_QUEUE1(7, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_QUEUE2(8, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_QUEUE3(9, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_QUEUE4(10, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_QUEUE5(11, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_QUEUE6(13, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_QUEUE7(14, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_QUEUE8(15, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_QUEUE9(16, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_QUEUE10(17, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_QUEUE11(18, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_QUEUE12(19, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_QUEUE13(20, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_QUEUE14(21, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_QUEUE15(22, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_QUEUE16(23, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_QUEUE17(24, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_QUEUE18(25, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_QUEUE19(26, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_QUEUE20(27, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),

    OPHELD1(28, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    OPHELD2(29, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    OPHELD3(30, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    OPHELD4(31, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    OPHELD5(32, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),

    APOBJ1(33, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    APOBJ2(34, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    APOBJ3(35, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    APOBJ4(36, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    APOBJ5(37, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    OPOBJ1(38, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    OPOBJ2(39, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    OPOBJ3(40, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    OPOBJ4(41, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    OPOBJ5(42, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),

    APLOC1(43, subjectMode = SubjectMode.Type(ScriptVarType.LOC)),
    APLOC2(44, subjectMode = SubjectMode.Type(ScriptVarType.LOC)),
    APLOC3(45, subjectMode = SubjectMode.Type(ScriptVarType.LOC)),
    APLOC4(46, subjectMode = SubjectMode.Type(ScriptVarType.LOC)),
    APLOC5(47, subjectMode = SubjectMode.Type(ScriptVarType.LOC)),
    OPLOC1(48, subjectMode = SubjectMode.Type(ScriptVarType.LOC)),
    OPLOC2(49, subjectMode = SubjectMode.Type(ScriptVarType.LOC)),
    OPLOC3(50, subjectMode = SubjectMode.Type(ScriptVarType.LOC)),
    OPLOC4(51, subjectMode = SubjectMode.Type(ScriptVarType.LOC)),
    OPLOC5(52, subjectMode = SubjectMode.Type(ScriptVarType.LOC)),

    WEAKQUEUE(53, allowParameters = true),
    QUEUE(54, allowParameters = true),

    DEBUGPROC(55),

    IF_BUTTON(56, subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT)),
    IF_CLOSE(57, subjectMode = SubjectMode.Type(ScriptVarType.INTERFACE)),
    IF_BUTTON1(58, subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT)),
    IF_BUTTON2(59, subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT)),
    IF_BUTTON3(60, subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT)),
    IF_BUTTON4(61, subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT)),
    IF_BUTTON5(62, subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT)),

    OPHELDU(63, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ, global = false)),
    OPHELDT(64, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ, global = false)),

    OPNPCU(65, subjectMode = SubjectMode.Type(ScriptVarType.NPC, global = false)),
    OPNPCT(66, subjectMode = SubjectMode.Type(ScriptVarType.NPC, global = false)),

    OPLOCU(67, subjectMode = SubjectMode.Type(ScriptVarType.LOC, global = false)),
    OPLOCT(68, subjectMode = SubjectMode.Type(ScriptVarType.LOC, global = false)),

    OPOBJU(69, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ, global = false)),
    OPOBJT(70, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ, global = false)),

    OPPLAYER1(69, subjectMode = SubjectMode.None),
    OPPLAYER2(70, subjectMode = SubjectMode.None),
    OPPLAYER3(71, subjectMode = SubjectMode.None),
    OPPLAYER4(72, subjectMode = SubjectMode.None),
    OPPLAYER5(73, subjectMode = SubjectMode.None),
    OPPLAYERU(74, subjectMode = SubjectMode.None),
    OPPLAYERT(75, subjectMode = SubjectMode.None),

    LOGIN(76, subjectMode = SubjectMode.None),
    LOGOUT(77, subjectMode = SubjectMode.None),
    MAPAREA(78),
    ;

    override val identifier: String get() = name.lowercase()
}
