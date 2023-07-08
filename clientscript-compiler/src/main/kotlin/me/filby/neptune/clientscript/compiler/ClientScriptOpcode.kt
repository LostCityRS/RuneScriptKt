package me.filby.neptune.clientscript.compiler

/**
 * An enumeration of opcode names with their id and how the operand is written in binary.
 *
 * This only contains the opcode and no information about the signatures of commands.
 */
enum class ClientScriptOpcode(val id: Int, val largeOperand: Boolean = false) {
    // Core language ops
    PUSH_CONSTANT_INT(0, true),
    PUSH_VARP(1, true),
    POP_VARP(2, true),
    PUSH_CONSTANT_STRING(3, true),
    BRANCH(6, true),
    BRANCH_NOT(7, true),
    BRANCH_EQUALS(8, true),
    BRANCH_LESS_THAN(9, true),
    BRANCH_GREATER_THAN(10, true),
    RETURN(21),
    PUSH_VARBIT(25, true),
    POP_VARBIT(27, true),
    BRANCH_LESS_THAN_OR_EQUALS(31, true),
    BRANCH_GREATER_THAN_OR_EQUALS(32, true),
    PUSH_INT_LOCAL(33, true),
    POP_INT_LOCAL(34, true),
    PUSH_STRING_LOCAL(35, true),
    POP_STRING_LOCAL(36, true),
    JOIN_STRING(37, true),
    POP_INT_DISCARD(38),
    POP_STRING_DISCARD(39),
    GOSUB_WITH_PARAMS(40, true),
    PUSH_VARC_INT(42, true),
    POP_VARC_INT(43, true),
    DEFINE_ARRAY(44, true),
    PUSH_ARRAY_INT(45, true),
    POP_ARRAY_INT(46, true),
    PUSH_VARC_STRING(49, true),
    POP_VARC_STRING(50, true),
    SWITCH(60, true),
    PUSH_VARCLANSETTING(74, true),
    PUSH_VARCLAN(76, true),

    GOSUB(90, true),
    JUMP(91, true),
    JUMP_WITH_PARAMS(92, true),

    STRONGQUEUE(1000),
    WEAKQUEUE(1001),

    // Server ops
    ANIM(2000),
    BUFFER_FULL(2001),
    BUILDAPPEARANCE(2002),
    CAM_LOOKAT(2003),
    CAM_MOVETO(2004),
    CAM_RESET(2005),
    COORD(2006),
    DISPLAYNAME(2007),
    DISTANCE(2008),
    ENUM_GETOUTPUTCOUNT(2009),
    FACESQUARE(2010),
    FINDUID(2011),
    HEALENERGY(2012),
    HUNTALL(2013),
    HUNTNEXT(2014),
    IF_CLOSE(2015),
    IF_OPENSUBMODAL(2016),
    IF_OPENSUBOVERLAY(2017),
    INAREA(2018),
    INV_ADD(2019),
    INV_CHANGESLOT(2020),
    INV_DEL(2021),
    INV_GETOBJ(2022),
    INV_ITEMSPACE2(2023),
    INV_MOVEITEM(2024),
    INV_RESENDSLOT(2025),
    INV_SETSLOT(2026),
    INV_SIZE(2027),
    INV_TOTAL(2028),
    INZONE(2029),
    LAST_COMSUBID(2030),
    LAST_INT(2031),
    LAST_ITEM(2032),
    LAST_SLOT(2033),
    LAST_USEITEM(2034),
    LAST_USESLOT(2035),
    LAST_VERIFYOBJ(2036),
    LINEOFWALK(2037),
    LOC_ADD(2038),
    LOC_ANGLE(2039),
    LOC_ANIM(2040),
    LOC_CATEGORY(2041),
    LOC_CHANGE(2042),
    LOC_COORD(2043),
    LOC_DEL(2044),
    LOC_FIND(2045),
    LOC_FINDALLZONE(2046),
    LOC_FINDNEXT(2047),
    LOC_PARAM(2048),
    LOC_TYPE(2049),
    MAP_CLOCK(2050),
    MAP_MEMBERS(2051),
    MAP_PLAYERCOUNT(2052),
    MES(2053),
    MOVECOORD(2054),
    NAME(2055),
    NPC_ADD(2056),
    NPC_ANIM(2057),
    NPC_BASESTAT(2058),
    NPC_CATEGORY(2059),
    NPC_COORD(2060),
    NPC_DEL(2061),
    NPC_DELAY(2062),
    NPC_FACESQUARE(2063),
    NPC_FINDEXACT(2064),
    NPC_FINDHERO(2065),
    NPC_PARAM(2066),
    NPC_QUEUE(2067),
    NPC_RANGE(2068),
    NPC_SAY(2069),
    NPC_SETHUNT(2070),
    NPC_SETHUNTMODE(2071),
    NPC_SETMODE(2072),
    NPC_STAT(2073),
    NPC_STATHEAL(2074),
    NPC_TYPE(2075),
    OBJ_ADD(2076),
    OBJ_ADDALL(2077),
    OBJECTVERIFY(2078),
    OC_CATEGORY(2079),
    OC_DESC(2080),
    OC_MEMBERS(2081),
    OC_PARAM(2082),
    OC_WEIGHT(2083),
    OC_WEARPOS(2084),
    P_APRANGE(2085),
    P_ARRIVEDELAY(2086),
    P_COUNTDIALOG(2087),
    P_DELAY(2088),
    P_OPHELD(2089),
    P_OPLOC(2090),
    P_OPNPC(2091),
    P_PAUSEBUTTON(2092),
    P_STOPACTION(2093),
    P_TELEJUMP(2094),
    P_WALK(2095),
    SAY(2096),
    SEQLENGTH(2097),
    SOUND_SYNTH(2098),
    SPLIT_INIT(2099),
    SPLIT_PAGE(2100),
    SPOTANIM_MAP(2101),
    STAFFMODLEVEL(2102),
    STAT(2103),
    STAT_HEAL(2104),
    STAT_RANDOM(2105),
    UID(2106),
    // --
    NC_PARAM(2107),
    ENUM(2108),
    P_LOGOUT(2109),
    OBJ_PARAM(2110),

    IF_CHATSELECT(2500),
    CHATNPC(2501),
    ERROR(2502),
    CHATPLAYER(2503),
    OBJBOX(2504),
    GIVEXP(2505),
    NPC_DAMAGE(2506), // player damaging npc
    DAMAGE(2507), // npc damaging player

    // Other ops
    ADD(4000),
    SUB(4001),
    MULTIPLY(4002),
    DIVIDE(4003),
    RANDOM(4004),
    RANDOMINC(4005),
    INTERPOLATE(4006),
    ADDPERCENT(4007),
    SETBIT(4008),
    CLEARBIT(4009),
    TESTBIT(4010),
    MODULO(4011),
    POW(4012),
    INVPOW(4013),
    AND(4014),
    OR(4015),
    MIN(4016),
    MAX(4017),
    SCALE(4018),
    BITCOUNT(4025),
    TOGGLEBIT(4026),
    SETBIT_RANGE(4027),
    CLEARBIT_RANGE(4028),
    GETBIT_RANGE(4029),
    SETBIT_RANGE_TOINT(4030),
    SIN_DEG(4032),
    COS_DEG(4033),
    ATAN2_DEG(4034),
    ABS(4035),
    PARSEINT(4036),
    APPEND_NUM(4100),
    APPEND(4101),
    APPEND_SIGNNUM(4102),
    LOWERCASE(4103),
    TOSTRING(4106),
    COMPARE(4107),
    ESCAPE(4111),
    APPEND_CHAR(4112),
    STRING_LENGTH(4117),
    SUBSTRING(4118),
    STRING_INDEXOF_CHAR(4120),
    STRING_INDEXOF_STRING(4121),
    UPPERCASE(4122),

    // Debug ops
    ACTIVE_NPC(10000),
    ACTIVE_PLAYER(10001),
    ACTIVE_LOC(10002),
    ACTIVE_OBJ(10003),
    ;

    companion object {
        val LOOKUP = values().associateBy { it.name.lowercase() }
    }
}
