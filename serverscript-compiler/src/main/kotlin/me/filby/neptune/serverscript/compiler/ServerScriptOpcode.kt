package me.filby.neptune.serverscript.compiler

/**
 * An enumeration of opcode names with their id and how the operand is written in binary.
 *
 * This only contains the opcode and no information about the signatures of commands.
 */
enum class ServerScriptOpcode(val id: Int, val largeOperand: Boolean = false) {
    // Core language ops (0-99)
    PUSH_CONSTANT_INT(0, true),
    PUSH_VARP(1, true),
    POP_VARP(2, true),
    PUSH_CONSTANT_STRING(3, true),
    PUSH_VARN(4, true),
    POP_VARN(5, true),
    BRANCH(6, true),
    BRANCH_NOT(7, true),
    BRANCH_EQUALS(8, true),
    BRANCH_LESS_THAN(9, true),
    BRANCH_GREATER_THAN(10, true),
    PUSH_VARS(11, true),
    POP_VARS(12, true),
    RETURN(21),
    GOSUB(22),
    JUMP(23),
    SWITCH(24, true),
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
    JUMP_WITH_PARAMS(41, true),
    DEFINE_ARRAY(44, true),
    PUSH_ARRAY_INT(45, true),
    POP_ARRAY_INT(46, true),

    // Number ops (4600-4699)
    ADD(4600),
    SUB(4601),
    MULTIPLY(4602),
    DIVIDE(4603),
    MODULO(4611),
    AND(4614),
    OR(4615),
}
