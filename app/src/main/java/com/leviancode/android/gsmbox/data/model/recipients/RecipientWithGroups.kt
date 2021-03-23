package com.leviancode.android.gsmbox.data.model.recipients

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class RecipientWithGroups(
    @Embedded var recipient: Recipient,
    @Relation(
        parentColumn = "recipientId",
        entityColumn = "recipientGroupId",
        associateBy = Junction(RecipientsAndGroupsCrossRef::class)
    )
    var groups: MutableList<RecipientGroup>
) {

    fun addGroup(group: RecipientGroup) {
        groups.add(group)
    }

    fun removeGroup(group: RecipientGroup) {
        groups.remove(group)
    }
}
