package me.filby.neptune.clientscript.compiler.trigger

import me.filby.neptune.clientscript.compiler.type.ScriptVarType
import me.filby.neptune.runescript.compiler.pointer.PointerType
import me.filby.neptune.runescript.compiler.trigger.SubjectMode
import me.filby.neptune.runescript.compiler.trigger.TriggerType
import me.filby.neptune.runescript.compiler.type.PrimitiveType
import me.filby.neptune.runescript.compiler.type.Type
import java.util.EnumSet

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
    override val pointers: Set<PointerType>? = null,
) : TriggerType {
    PROC(0, allowParameters = true, allowReturns = true, pointers = EnumSet.allOf(PointerType::class.java)),
    LABEL(1, allowParameters = true, pointers = EnumSet.allOf(PointerType::class.java)),
    DEBUGPROC(2, allowParameters = true, pointers = EnumSet.of(PointerType.ACTIVE_PLAYER)),

    APNPC1(
        3,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_NPC)
    ),
    APNPC2(
        4,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_NPC)
    ),
    APNPC3(
        5,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_NPC)
    ),
    APNPC4(
        6,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_NPC)
    ),
    APNPC5(
        7,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_NPC)
    ),
    APNPCU(
        8,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(
            PointerType.ACTIVE_PLAYER,
            PointerType.P_ACTIVE_PLAYER,
            PointerType.LAST_USEITEM,
            PointerType.LAST_USESLOT,
            PointerType.ACTIVE_NPC
        )
    ),
    APNPCT(
        9,
        subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT, category = false, global = false),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_NPC)
    ),
    OPNPC1(
        10,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_NPC)
    ),
    OPNPC2(
        11,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_NPC)
    ),
    OPNPC3(
        12,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_NPC)
    ),
    OPNPC4(
        13,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_NPC)
    ),
    OPNPC5(
        14,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_NPC)
    ),
    OPNPCU(
        15,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(
            PointerType.ACTIVE_PLAYER,
            PointerType.P_ACTIVE_PLAYER,
            PointerType.LAST_USEITEM,
            PointerType.LAST_USESLOT,
            PointerType.ACTIVE_NPC
        )
    ),
    OPNPCT(
        16,
        subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT, category = false, global = false),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_NPC)
    ),
    AI_APNPC1(
        17,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_NPC2)
    ),
    AI_APNPC2(
        18,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_NPC2)
    ),
    AI_APNPC3(
        19,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_NPC2)
    ),
    AI_APNPC4(
        20,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_NPC2)
    ),
    AI_APNPC5(
        21,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_NPC2)
    ),
    AI_OPNPC1(
        24,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_NPC2)
    ),
    AI_OPNPC2(
        25,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_NPC2)
    ),
    AI_OPNPC3(
        26,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_NPC2)
    ),
    AI_OPNPC4(
        27,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_NPC2)
    ),
    AI_OPNPC5(
        28,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_NPC2)
    ),

    APOBJ1(
        31,
        subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_OBJ)
    ),
    APOBJ2(
        32,
        subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_OBJ)
    ),
    APOBJ3(
        33,
        subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_OBJ)
    ),
    APOBJ4(
        34,
        subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_OBJ)
    ),
    APOBJ5(
        35,
        subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_OBJ)
    ),
    APOBJU(
        36,
        subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ),
        pointers = EnumSet.of(
            PointerType.ACTIVE_PLAYER,
            PointerType.P_ACTIVE_PLAYER,
            PointerType.LAST_USEITEM,
            PointerType.LAST_USESLOT,
            PointerType.ACTIVE_OBJ
        )
    ),
    APOBJT(
        37,
        subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT, category = false, global = false),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_OBJ)
    ),
    OPOBJ1(
        38,
        subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_OBJ)
    ),
    OPOBJ2(
        39,
        subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_OBJ)
    ),
    OPOBJ3(
        40,
        subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_OBJ)
    ),
    OPOBJ4(
        41,
        subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_OBJ)
    ),
    OPOBJ5(
        42,
        subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_OBJ)
    ),
    OPOBJU(
        43,
        subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ),
        pointers = EnumSet.of(
            PointerType.ACTIVE_PLAYER,
            PointerType.P_ACTIVE_PLAYER,
            PointerType.LAST_USEITEM,
            PointerType.LAST_USESLOT,
            PointerType.ACTIVE_OBJ
        )
    ),
    OPOBJT(
        44,
        subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT, category = false, global = false),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_OBJ)
    ),
    AI_APOBJ1(
        45,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_OBJ)
    ),
    AI_APOBJ2(
        46,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_OBJ)
    ),
    AI_APOBJ3(
        47,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_OBJ)
    ),
    AI_APOBJ4(
        48,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_OBJ)
    ),
    AI_APOBJ5(
        49,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_OBJ)
    ),
    AI_OPOBJ1(
        52,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_OBJ)
    ),
    AI_OPOBJ2(
        53,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_OBJ)
    ),
    AI_OPOBJ3(
        54,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_OBJ)
    ),
    AI_OPOBJ4(
        55,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_OBJ)
    ),
    AI_OPOBJ5(
        56,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_OBJ)
    ),

    APLOC1(
        59,
        subjectMode = SubjectMode.Type(ScriptVarType.LOC),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_LOC)
    ),
    APLOC2(
        60,
        subjectMode = SubjectMode.Type(ScriptVarType.LOC),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_LOC)
    ),
    APLOC3(
        61,
        subjectMode = SubjectMode.Type(ScriptVarType.LOC),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_LOC)
    ),
    APLOC4(
        62,
        subjectMode = SubjectMode.Type(ScriptVarType.LOC),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_LOC)
    ),
    APLOC5(
        63,
        subjectMode = SubjectMode.Type(ScriptVarType.LOC),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_LOC)
    ),
    APLOCU(
        64,
        subjectMode = SubjectMode.Type(ScriptVarType.LOC),
        pointers = EnumSet.of(
            PointerType.ACTIVE_PLAYER,
            PointerType.P_ACTIVE_PLAYER,
            PointerType.LAST_USEITEM,
            PointerType.LAST_USESLOT,
            PointerType.ACTIVE_LOC
        )
    ),
    APLOCT(
        65,
        subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT, category = false, global = false),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_LOC)
    ),
    OPLOC1(
        66,
        subjectMode = SubjectMode.Type(ScriptVarType.LOC),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_LOC)
    ),
    OPLOC2(
        67,
        subjectMode = SubjectMode.Type(ScriptVarType.LOC),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_LOC)
    ),
    OPLOC3(
        68,
        subjectMode = SubjectMode.Type(ScriptVarType.LOC),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_LOC)
    ),
    OPLOC4(
        69,
        subjectMode = SubjectMode.Type(ScriptVarType.LOC),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_LOC)
    ),
    OPLOC5(
        70,
        subjectMode = SubjectMode.Type(ScriptVarType.LOC),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_LOC)
    ),
    OPLOCU(
        71,
        subjectMode = SubjectMode.Type(ScriptVarType.LOC),
        pointers = EnumSet.of(
            PointerType.ACTIVE_PLAYER,
            PointerType.P_ACTIVE_PLAYER,
            PointerType.LAST_USEITEM,
            PointerType.LAST_USESLOT,
            PointerType.ACTIVE_LOC
        )
    ),
    OPLOCT(
        72,
        subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT, category = false, global = false),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_LOC)
    ),
    AI_APLOC1(
        73,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_LOC)
    ),
    AI_APLOC2(
        74,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_LOC)
    ),
    AI_APLOC3(
        75,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_LOC)
    ),
    AI_APLOC4(
        76,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_LOC)
    ),
    AI_APLOC5(
        77,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_LOC)
    ),
    AI_OPLOC1(
        80,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_LOC)
    ),
    AI_OPLOC2(
        81,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_LOC)
    ),
    AI_OPLOC3(
        82,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_LOC)
    ),
    AI_OPLOC4(
        83,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_LOC)
    ),
    AI_OPLOC5(
        84,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_LOC)
    ),

    APPLAYER1(
        87,
        subjectMode = SubjectMode.None,
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_PLAYER2)
    ),
    APPLAYER2(
        88,
        subjectMode = SubjectMode.None,
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_PLAYER2)
    ),
    APPLAYER3(
        89,
        subjectMode = SubjectMode.None,
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_PLAYER2)
    ),
    APPLAYER4(
        90,
        subjectMode = SubjectMode.None,
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_PLAYER2)
    ),
    APPLAYER5(
        91,
        subjectMode = SubjectMode.None,
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_PLAYER2)
    ),
    APPLAYERU(
        92,
        subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ, global = false),
        pointers = EnumSet.of(
            PointerType.ACTIVE_PLAYER,
            PointerType.P_ACTIVE_PLAYER,
            PointerType.LAST_USEITEM,
            PointerType.LAST_USESLOT,
            PointerType.ACTIVE_PLAYER2
        )
    ),
    APPLAYERT(
        93,
        subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT, category = false, global = false),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_PLAYER2)
    ),
    OPPLAYER1(
        94,
        subjectMode = SubjectMode.None,
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_PLAYER2)
    ),
    OPPLAYER2(
        95,
        subjectMode = SubjectMode.None,
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_PLAYER2)
    ),
    OPPLAYER3(
        96,
        subjectMode = SubjectMode.None,
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_PLAYER2)
    ),
    OPPLAYER4(
        97,
        subjectMode = SubjectMode.None,
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_PLAYER2)
    ),
    OPPLAYER5(
        98,
        subjectMode = SubjectMode.None,
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_PLAYER2)
    ),
    OPPLAYERU(
        99,
        subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ, global = false),
        pointers = EnumSet.of(
            PointerType.ACTIVE_PLAYER,
            PointerType.P_ACTIVE_PLAYER,
            PointerType.LAST_USEITEM,
            PointerType.LAST_USESLOT,
            PointerType.ACTIVE_PLAYER2
        )
    ),
    OPPLAYERT(
        100,
        subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT, category = false, global = false),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.ACTIVE_PLAYER2)
    ),
    AI_APPLAYER1(
        101,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_PLAYER)
    ),
    AI_APPLAYER2(
        102,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_PLAYER)
    ),
    AI_APPLAYER3(
        103,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_PLAYER)
    ),
    AI_APPLAYER4(
        104,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_PLAYER)
    ),
    AI_APPLAYER5(
        105,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_PLAYER)
    ),
    AI_OPPLAYER1(
        108,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_PLAYER)
    ),
    AI_OPPLAYER2(
        109,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_PLAYER)
    ),
    AI_OPPLAYER3(
        110,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_PLAYER)
    ),
    AI_OPPLAYER4(
        111,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_PLAYER)
    ),
    AI_OPPLAYER5(
        112,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.ACTIVE_PLAYER)
    ),

    QUEUE(116, allowParameters = true, pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER)),
    AI_QUEUE1(
        117,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.LAST_INT)
    ),
    AI_QUEUE2(
        118,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.LAST_INT)
    ),
    AI_QUEUE3(
        119,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.LAST_INT)
    ),
    AI_QUEUE4(
        120,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.LAST_INT)
    ),
    AI_QUEUE5(
        121,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.LAST_INT)
    ),
    AI_QUEUE6(
        122,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.LAST_INT)
    ),
    AI_QUEUE7(
        123,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.LAST_INT)
    ),
    AI_QUEUE8(
        124,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.LAST_INT)
    ),
    AI_QUEUE9(
        125,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.LAST_INT)
    ),
    AI_QUEUE10(
        126,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.LAST_INT)
    ),
    AI_QUEUE11(
        127,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.LAST_INT)
    ),
    AI_QUEUE12(
        128,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.LAST_INT)
    ),
    AI_QUEUE13(
        129,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.LAST_INT)
    ),
    AI_QUEUE14(
        130,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.LAST_INT)
    ),
    AI_QUEUE15(
        131,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.LAST_INT)
    ),
    AI_QUEUE16(
        132,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.LAST_INT)
    ),
    AI_QUEUE17(
        133,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.LAST_INT)
    ),
    AI_QUEUE18(
        134,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.LAST_INT)
    ),
    AI_QUEUE19(
        135,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.LAST_INT)
    ),
    AI_QUEUE20(
        136,
        subjectMode = SubjectMode.Type(ScriptVarType.NPC),
        pointers = EnumSet.of(PointerType.ACTIVE_NPC, PointerType.LAST_INT)
    ),

    SOFTTIMER(137, allowParameters = true, pointers = EnumSet.of(PointerType.ACTIVE_PLAYER)),
    TIMER(138, allowParameters = true, pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER)),
    AI_TIMER(139, subjectMode = SubjectMode.Type(ScriptVarType.NPC), pointers = EnumSet.of(PointerType.ACTIVE_NPC)),

    OPHELD1(
        140,
        subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ),
        pointers = EnumSet.of(
            PointerType.ACTIVE_PLAYER,
            PointerType.P_ACTIVE_PLAYER,
            PointerType.LAST_ITEM,
            PointerType.LAST_SLOT
        )
    ),
    OPHELD2(
        141,
        subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ),
        pointers = EnumSet.of(
            PointerType.ACTIVE_PLAYER,
            PointerType.P_ACTIVE_PLAYER,
            PointerType.LAST_ITEM,
            PointerType.LAST_SLOT
        )
    ),
    OPHELD3(
        142,
        subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ),
        pointers = EnumSet.of(
            PointerType.ACTIVE_PLAYER,
            PointerType.P_ACTIVE_PLAYER,
            PointerType.LAST_ITEM,
            PointerType.LAST_SLOT
        )
    ),
    OPHELD4(
        143,
        subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ),
        pointers = EnumSet.of(
            PointerType.ACTIVE_PLAYER,
            PointerType.P_ACTIVE_PLAYER,
            PointerType.LAST_ITEM,
            PointerType.LAST_SLOT
        )
    ),
    OPHELD5(
        144,
        subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ),
        pointers = EnumSet.of(
            PointerType.ACTIVE_PLAYER,
            PointerType.P_ACTIVE_PLAYER,
            PointerType.LAST_ITEM,
            PointerType.LAST_SLOT
        )
    ),
    OPHELDU(
        145,
        subjectMode = SubjectMode.Type(ScriptVarType.NAMEDOBJ),
        pointers = EnumSet.of(
            PointerType.ACTIVE_PLAYER,
            PointerType.P_ACTIVE_PLAYER,
            PointerType.LAST_ITEM,
            PointerType.LAST_SLOT,
            PointerType.LAST_USEITEM,
            PointerType.LAST_USESLOT
        )
    ),
    OPHELDT(
        146,
        subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT, category = false, global = false),
        pointers = EnumSet.of(
            PointerType.ACTIVE_PLAYER,
            PointerType.P_ACTIVE_PLAYER,
            PointerType.LAST_ITEM,
            PointerType.LAST_SLOT
        )
    ),

    IF_BUTTON(
        147,
        subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER, PointerType.LAST_COM)
    ),
    INV_BUTTON1(
        148,
        subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT),
        pointers = EnumSet.of(
            PointerType.ACTIVE_PLAYER,
            PointerType.P_ACTIVE_PLAYER,
            PointerType.LAST_ITEM,
            PointerType.LAST_SLOT,
        )
    ),
    INV_BUTTON2(
        149,
        subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT),
        pointers = EnumSet.of(
            PointerType.ACTIVE_PLAYER,
            PointerType.P_ACTIVE_PLAYER,
            PointerType.LAST_ITEM,
            PointerType.LAST_SLOT,
        )
    ),
    INV_BUTTON3(
        150,
        subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT),
        pointers = EnumSet.of(
            PointerType.ACTIVE_PLAYER,
            PointerType.P_ACTIVE_PLAYER,
            PointerType.LAST_ITEM,
            PointerType.LAST_SLOT,
        )
    ),
    INV_BUTTON4(
        151,
        subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT),
        pointers = EnumSet.of(
            PointerType.ACTIVE_PLAYER,
            PointerType.P_ACTIVE_PLAYER,
            PointerType.LAST_ITEM,
            PointerType.LAST_SLOT,
        )
    ),
    INV_BUTTON5(
        152,
        subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT),
        pointers = EnumSet.of(
            PointerType.ACTIVE_PLAYER,
            PointerType.P_ACTIVE_PLAYER,
            PointerType.LAST_ITEM,
            PointerType.LAST_SLOT,
        )
    ),
    INV_BUTTOND(
        153,
        subjectMode = SubjectMode.Type(ScriptVarType.COMPONENT),
        pointers = EnumSet.of(
            PointerType.ACTIVE_PLAYER,
            PointerType.P_ACTIVE_PLAYER,
            PointerType.LAST_SLOT,
            PointerType.LAST_TARGETSLOT
        )
    ),
    IF_CLOSE(
        154,
        subjectMode = SubjectMode.Type(ScriptVarType.INTERFACE),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER)
    ),

    LOGIN(
        155,
        subjectMode = SubjectMode.None,
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER)
    ),
    LOGOUT(
        156,
        subjectMode = SubjectMode.None,
        allowReturns = true,
        returns = PrimitiveType.BOOLEAN,
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER)
    ),
    TUTORIAL_CLICKSIDE(
        157,
        subjectMode = SubjectMode.None,
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER)
    ), // clicking a tab during tutorial

    // may not exist at this time? area (per-tile triggers) exists later for sure
    MOVE(
        158,
        subjectMode = SubjectMode.None,
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER)
    ),
    WALKTRIGGER(
        159,
        subjectMode = SubjectMode.Name,
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER)
    ),
    AI_WALKTRIGGER(
        160,
        subjectMode = SubjectMode.Name,
        pointers = EnumSet.of(PointerType.ACTIVE_NPC)
    ),

    LEVELUP(
        161,
        subjectMode = SubjectMode.Type(ScriptVarType.STAT),
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER)
    ),

    // just in case we need something to fire on entering a mapsquare later
    MAPTRIGGER(
        162,
        subjectMode = SubjectMode.None,
        pointers = EnumSet.of(PointerType.ACTIVE_PLAYER, PointerType.P_ACTIVE_PLAYER)
    )
    ;

    override val identifier: String get() = name.lowercase()
}
