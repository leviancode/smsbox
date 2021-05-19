package com.leviancode.android.gsmbox.core.data.model.recipients

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["recipientGroupId", "recipientId"],
    tableName = "recipients_and_groups",
    foreignKeys = [ForeignKey(
        entity = RecipientGroup::class,
        parentColumns = arrayOf("recipientGroupId"),
        childColumns = arrayOf("recipientGroupId"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE,
        deferred = true
    ),
        ForeignKey(
            entity = Recipient::class,
            parentColumns = arrayOf("recipientId"),
            childColumns = arrayOf("recipientId"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            deferred = true
        )]

)
data class RecipientsAndGroupRelation(
    val recipientGroupId: Int,
    val recipientId: Int
)
