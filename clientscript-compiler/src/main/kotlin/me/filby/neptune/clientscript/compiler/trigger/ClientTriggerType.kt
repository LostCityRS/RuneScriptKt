package me.filby.neptune.clientscript.compiler.trigger

import me.filby.neptune.clientscript.compiler.type.ScriptVarType
import me.filby.neptune.runescript.compiler.trigger.SubjectMode
import me.filby.neptune.runescript.compiler.trigger.TriggerType
import me.filby.neptune.runescript.compiler.type.PrimitiveType
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
    DEBUGPROC(2, allowParameters = true),

    APNPC1(3, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    APNPC2(4, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    APNPC3(5, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    APNPC4(6, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    APNPC5(7, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    APNPCU(8, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    APNPCT(9, subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT, category = false, global = false)),
    OPNPC1(10, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    OPNPC2(11, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    OPNPC3(12, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    OPNPC4(13, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    OPNPC5(14, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    OPNPCU(15, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    OPNPCT(16, subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT, category = false, global = false)),
    AI_APNPC1(17, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_APNPC2(18, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_APNPC3(19, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_APNPC4(20, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_APNPC5(21, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_OPNPC1(24, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_OPNPC2(25, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_OPNPC3(26, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_OPNPC4(27, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_OPNPC5(28, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),

    APOBJ1(31, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    APOBJ2(32, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    APOBJ3(33, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    APOBJ4(34, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    APOBJ5(35, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    APOBJU(36, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    APOBJT(37, subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT, category = false, global = false)),
    OPOBJ1(38, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    OPOBJ2(39, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    OPOBJ3(40, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    OPOBJ4(41, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    OPOBJ5(42, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    OPOBJU(43, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    OPOBJT(44, subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT, category = false, global = false)),
    AI_APOBJ1(45, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_APOBJ2(46, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_APOBJ3(47, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_APOBJ4(48, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_APOBJ5(49, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_OPOBJ1(52, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_OPOBJ2(53, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_OPOBJ3(54, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_OPOBJ4(55, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_OPOBJ5(56, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),

    APLOC1(59, subjectMode = SubjectMode.Type(ScriptVarType.LOC)),
    APLOC2(60, subjectMode = SubjectMode.Type(ScriptVarType.LOC)),
    APLOC3(61, subjectMode = SubjectMode.Type(ScriptVarType.LOC)),
    APLOC4(62, subjectMode = SubjectMode.Type(ScriptVarType.LOC)),
    APLOC5(63, subjectMode = SubjectMode.Type(ScriptVarType.LOC)),
    APLOCU(64, subjectMode = SubjectMode.Type(ScriptVarType.LOC)),
    APLOCT(65, subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT, category = false, global = false)),
    OPLOC1(66, subjectMode = SubjectMode.Type(ScriptVarType.LOC)),
    OPLOC2(67, subjectMode = SubjectMode.Type(ScriptVarType.LOC)),
    OPLOC3(68, subjectMode = SubjectMode.Type(ScriptVarType.LOC)),
    OPLOC4(69, subjectMode = SubjectMode.Type(ScriptVarType.LOC)),
    OPLOC5(70, subjectMode = SubjectMode.Type(ScriptVarType.LOC)),
    OPLOCU(71, subjectMode = SubjectMode.Type(ScriptVarType.LOC)),
    OPLOCT(72, subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT, category = false, global = false)),
    AI_APLOC1(73, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_APLOC2(74, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_APLOC3(75, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_APLOC4(76, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_APLOC5(77, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_OPLOC1(80, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_OPLOC2(81, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_OPLOC3(82, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_OPLOC4(83, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_OPLOC5(84, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),

    APPLAYER1(87, subjectMode = SubjectMode.None),
    APPLAYER2(88, subjectMode = SubjectMode.None),
    APPLAYER3(89, subjectMode = SubjectMode.None),
    APPLAYER4(90, subjectMode = SubjectMode.None),
    APPLAYER5(91, subjectMode = SubjectMode.None),
    APPLAYERU(92, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ, global = false)),
    APPLAYERT(93, subjectMode = SubjectMode.None),
    OPPLAYER1(94, subjectMode = SubjectMode.None),
    OPPLAYER2(95, subjectMode = SubjectMode.None),
    OPPLAYER3(96, subjectMode = SubjectMode.None),
    OPPLAYER4(97, subjectMode = SubjectMode.None),
    OPPLAYER5(98, subjectMode = SubjectMode.None),
    OPPLAYERU(99, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ, global = false)),
    OPPLAYERT(100, subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT, category = false, global = false)),
    AI_APPLAYER1(101, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_APPLAYER2(102, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_APPLAYER3(103, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_APPLAYER4(104, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_APPLAYER5(105, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_OPPLAYER1(108, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_OPPLAYER2(109, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_OPPLAYER3(110, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_OPPLAYER4(111, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),
    AI_OPPLAYER5(112, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),

    WEAKQUEUE(115, allowParameters = true),
    QUEUE(116, allowParameters = true),
    AI_QUEUE1(117, subjectMode = SubjectMode.Type(ScriptVarType.NPC), parameters = PrimitiveType.INT),
    AI_QUEUE2(118, subjectMode = SubjectMode.Type(ScriptVarType.NPC), parameters = PrimitiveType.INT),
    AI_QUEUE3(119, subjectMode = SubjectMode.Type(ScriptVarType.NPC), parameters = PrimitiveType.INT),
    AI_QUEUE4(120, subjectMode = SubjectMode.Type(ScriptVarType.NPC), parameters = PrimitiveType.INT),
    AI_QUEUE5(121, subjectMode = SubjectMode.Type(ScriptVarType.NPC), parameters = PrimitiveType.INT),
    AI_QUEUE6(122, subjectMode = SubjectMode.Type(ScriptVarType.NPC), parameters = PrimitiveType.INT),
    AI_QUEUE7(123, subjectMode = SubjectMode.Type(ScriptVarType.NPC), parameters = PrimitiveType.INT),
    AI_QUEUE8(124, subjectMode = SubjectMode.Type(ScriptVarType.NPC), parameters = PrimitiveType.INT),
    AI_QUEUE9(125, subjectMode = SubjectMode.Type(ScriptVarType.NPC), parameters = PrimitiveType.INT),
    AI_QUEUE10(126, subjectMode = SubjectMode.Type(ScriptVarType.NPC), parameters = PrimitiveType.INT),
    AI_QUEUE11(127, subjectMode = SubjectMode.Type(ScriptVarType.NPC), parameters = PrimitiveType.INT),
    AI_QUEUE12(128, subjectMode = SubjectMode.Type(ScriptVarType.NPC), parameters = PrimitiveType.INT),
    AI_QUEUE13(129, subjectMode = SubjectMode.Type(ScriptVarType.NPC), parameters = PrimitiveType.INT),
    AI_QUEUE14(130, subjectMode = SubjectMode.Type(ScriptVarType.NPC), parameters = PrimitiveType.INT),
    AI_QUEUE15(131, subjectMode = SubjectMode.Type(ScriptVarType.NPC), parameters = PrimitiveType.INT),
    AI_QUEUE16(132, subjectMode = SubjectMode.Type(ScriptVarType.NPC), parameters = PrimitiveType.INT),
    AI_QUEUE17(133, subjectMode = SubjectMode.Type(ScriptVarType.NPC), parameters = PrimitiveType.INT),
    AI_QUEUE18(134, subjectMode = SubjectMode.Type(ScriptVarType.NPC), parameters = PrimitiveType.INT),
    AI_QUEUE19(135, subjectMode = SubjectMode.Type(ScriptVarType.NPC), parameters = PrimitiveType.INT),
    AI_QUEUE20(136, subjectMode = SubjectMode.Type(ScriptVarType.NPC), parameters = PrimitiveType.INT),

    SOFTTIMER(137, allowParameters = true),
    TIMER(138, allowParameters = true),
    AI_TIMER(139, subjectMode = SubjectMode.Type(ScriptVarType.NPC)),

    OPHELD1(140, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    OPHELD2(141, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    OPHELD3(142, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    OPHELD4(143, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    OPHELD5(144, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    OPHELDU(145, subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ)),
    OPHELDT(146, subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT, category = false, global = false)),

    IF_BUTTON(147, subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT)),
    INV_BUTTON1(148, subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT)),
    INV_BUTTON2(149, subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT)),
    INV_BUTTON3(150, subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT)),
    INV_BUTTON4(151, subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT)),
    INV_BUTTON5(152, subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT)),
    INV_BUTTOND(153, subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT)),
    IF_CLOSE(154, subjectMode = SubjectMode.Type(ScriptVarType.INTERFACE)),

    LOGIN(155, subjectMode = SubjectMode.None),
    LOGOUT(156, subjectMode = SubjectMode.None),
    MAPENTER(157, subjectMode = SubjectMode.None), // entering a mapsquare
    IF_FLASHING_TAB(158, subjectMode = SubjectMode.None), // clicking a tab during tutorial

    MOVE(159, subjectMode = SubjectMode.None), // movement trigger
    MOVECHECK(160, subjectMode = SubjectMode.Name, returns = PrimitiveType.BOOLEAN), // pre-movement trigger
    AI_MOVECHECK(161, subjectMode = SubjectMode.Name, returns = PrimitiveType.BOOLEAN),

    LEVELUP(162, subjectMode = SubjectMode.Type(ScriptVarType.STAT)),
    ;

    override val identifier: String get() = name.lowercase()
}
