package com.leviancode.android.gsmbox.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class RecipientGroupAndRecipients(
    @Embedded val group: RecipientGroup,
    @Relation(
        parentColumn = "groupId",
        entityColumn = "groupId"
    )
    val recipients: List<Recipient>
)
