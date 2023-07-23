package me.filby.neptune.clientscript.compiler

/**
 * An enumeration of opcode names with their id and how the operand is written in binary.
 *
 * This only contains the opcode and no information about the signatures of commands.
 */
enum class ClientScriptOpcode(val id: Int, val largeOperand: Boolean = false) {
    // Core language ops (0-99)
    PUSH_CONSTANT_INT(0, true),
    PUSH_CONSTANT_STRING(1, true),
    PUSH_VARP(2, true),
    POP_VARP(3, true),
    PUSH_VARN(4, true),
    POP_VARN(5, true),
    PUSH_INT_LOCAL(6, true),
    POP_INT_LOCAL(7, true),
    PUSH_STRING_LOCAL(8, true),
    POP_STRING_LOCAL(9, true),
    BRANCH(10, true),
    BRANCH_NOT(11, true),
    BRANCH_EQUALS(12, true),
    BRANCH_LESS_THAN(13, true),
    BRANCH_GREATER_THAN(14, true),
    BRANCH_LESS_THAN_OR_EQUALS(15, true),
    BRANCH_GREATER_THAN_OR_EQUALS(16, true),
    POP_INT_DISCARD(17),
    POP_STRING_DISCARD(18),
    RETURN(19),
    JOIN_STRING(20, true),
    GOSUB(21),
    GOSUB_WITH_PARAMS(22, true),
    JUMP(23),
    JUMP_WITH_PARAMS(24, true),
    DEFINE_ARRAY(25, true),
    PUSH_ARRAY_INT(26, true),
    POP_ARRAY_INT(27, true),
    SWITCH(28, true),

    // Number ops (4600-4699)
    ADD(4600),
    SUB(4601),
    MULTIPLY(4602),
    DIVIDE(4603),
    MODULO(4611),
    AND(4614),
    OR(4615),
}
