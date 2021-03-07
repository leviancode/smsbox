package com.leviancode.android.gsmbox.data.model.recipients

import androidx.room.Embedded
import androidx.room.Relation

data class RecipientGroupWithRecipients(
    @Embedded val group: RecipientGroup,
    @Relation(
        parentColumn = "groupName",
        entityColumn = "groupName"
    )
    val recipients: List<Recipient>
)
