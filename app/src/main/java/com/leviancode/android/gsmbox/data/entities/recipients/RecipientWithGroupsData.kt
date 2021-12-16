package com.leviancode.android.gsmbox.data.entities.recipients

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class RecipientWithGroupsData(
    @Embedded var recipient: RecipientData,
    @Relation(
        parentColumn = "recipientId",
        entityColumn = "recipientGroupId",
        associateBy = Junction(RecipientsAndGroupRelation::class)
    )
    var groups: List<RecipientGroupData>
)
