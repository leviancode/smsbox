package com.leviancode.android.gsmbox.ui.entities.recipients

data class RecipientWithGroupsUI(
    var recipient: RecipientUI = RecipientUI(),
    var groups: MutableList<RecipientGroupUI> = mutableListOf()
){
    fun addGroup(group: RecipientGroupUI) {
        groups.add(group)
    }

    fun removeGroup(group: RecipientGroupUI) {
        groups.remove(group)
    }

    fun getGroupsIds() = groups.map { it.id }

    fun copy() = RecipientWithGroupsUI(
        recipient = recipient.copy(),
        groups = groups.map { it.copy() }.toMutableList()
    )
}
