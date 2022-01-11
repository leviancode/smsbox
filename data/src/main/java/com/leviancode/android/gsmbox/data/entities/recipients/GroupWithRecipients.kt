package com.leviancode.android.gsmbox.data.entities.recipients

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class GroupWithRecipients(
    @Embedded var group: RecipientGroupData = RecipientGroupData(),
    @Relation(
        parentColumn = "recipientGroupId",
        entityColumn = "recipientId",
        associateBy = Junction(RecipientsAndGroupRelation::class)
    )
    var recipients: List<RecipientData> = listOf()
)