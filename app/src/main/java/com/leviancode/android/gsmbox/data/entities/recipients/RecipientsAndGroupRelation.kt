package com.leviancode.android.gsmbox.data.entities.recipients

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["recipientGroupId", "recipientId"],
    tableName = "recipients_and_groups",
    foreignKeys = [ForeignKey(
        entity = RecipientGroupData::class,
        parentColumns = arrayOf("recipientGroupId"),
        childColumns = arrayOf("recipientGroupId"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE,
        deferred = true
    ),
        ForeignKey(
            entity = RecipientData::class,
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
