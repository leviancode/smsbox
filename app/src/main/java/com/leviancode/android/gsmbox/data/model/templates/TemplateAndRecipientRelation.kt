package com.leviancode.android.gsmbox.data.model.templates

import androidx.room.Entity
import androidx.room.ForeignKey
import com.leviancode.android.gsmbox.data.model.recipients.Recipient

@Entity(
    primaryKeys = ["templateId", "recipientId"],
    tableName = "templates_and_recipients",
    foreignKeys = [
        ForeignKey(
            entity = Template::class,
            parentColumns = arrayOf("templateId"),
            childColumns = arrayOf("templateId"),
            onDelete = ForeignKey.CASCADE,
            deferred = true
        ),
        ForeignKey(
            entity = Recipient::class,
            parentColumns = arrayOf("recipientId"),
            childColumns = arrayOf("recipientId"),
            onDelete = ForeignKey.CASCADE,
            deferred = true
        )
    ]
)
data class TemplateAndRecipientRelation(
    val templateId: Long,
    val recipientId: Long
)

