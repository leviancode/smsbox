package com.leviancode.android.gsmbox.data.model.recipients

import androidx.room.Entity

@Entity(primaryKeys = ["recipientGroupId", "recipientId"], tableName = "recipients_and_groups")
data class RecipientsAndGroupsCrossRef(
    val recipientGroupId: String,
    val recipientId: String
)
