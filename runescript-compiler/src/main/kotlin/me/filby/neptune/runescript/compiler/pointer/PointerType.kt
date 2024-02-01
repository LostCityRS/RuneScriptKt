package me.filby.neptune.runescript.compiler.pointer

public enum class PointerType(public val representation: String) {
    ACTIVE_PLAYER("active_player"),
    ACTIVE_PLAYER2(".active_player"),
    P_ACTIVE_PLAYER("p_active_player"),
    P_ACTIVE_PLAYER2(".p_active_player"),
    ACTIVE_NPC("active_npc"),
    ACTIVE_NPC2(".active_npc"),
    ACTIVE_LOC("active_loc"),
    ACTIVE_LOC2(".active_loc"),
    ACTIVE_OBJ("active_obj"),
    ACTIVE_OBJ2(".active_obj"),
    FIND_PLAYER("find_player"),
    FIND_NPC("find_npc"),
    FIND_LOC("find_loc"),
    FIND_OBJ("find_obj"),
    FIND_DB("find_db"),
    LAST_COM("last_com"),
    LAST_INT("last_int"),
    LAST_ITEM("last_item"),
    LAST_SLOT("last_slot"),
    LAST_TARGETSLOT("last_targetslot"),
    LAST_USEITEM("last_useitem"),
    LAST_USESLOT("last_useslot"),
    ;

    public companion object {
        private val NAME_TO_TYPE = PointerType.values().associateBy { it.name.lowercase() }

        public fun forName(name: String): PointerType? {
            return NAME_TO_TYPE[name]
        }
    }
}
