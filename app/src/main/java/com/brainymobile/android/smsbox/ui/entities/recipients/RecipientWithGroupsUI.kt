package com.brainymobile.android.smsbox.ui.entities.recipients

data class RecipientWithGroupsUI(
    var recipient: RecipientUI = RecipientUI(),
    var groups: MutableList<RecipientGroupUI> = mutableListOf()
){
    val namedGroups: List<RecipientGroupUI>
        get() = groups.filter { it.isGroupNameNotNull() }

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
