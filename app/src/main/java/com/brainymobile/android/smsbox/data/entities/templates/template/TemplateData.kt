package com.brainymobile.android.smsbox.data.entities.templates.template

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.brainymobile.android.smsbox.data.entities.recipients.RecipientGroupData
import com.brainymobile.android.smsbox.data.entities.templates.group.TemplateGroupData
import com.brainymobile.android.smsbox.utils.DEFAULT_TEMPLATE_COLOR


@Entity(tableName = "templates",
    foreignKeys = [ForeignKey(
        entity = RecipientGroupData::class,
        parentColumns = arrayOf("recipientGroupId"),
        childColumns = arrayOf("recipientGroupId"),
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.RESTRICT,
        deferred = true
    ), ForeignKey(
            entity = TemplateGroupData::class,
            parentColumns = arrayOf("templateGroupId"),
            childColumns = arrayOf("templateGroupId"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.RESTRICT,
            deferred = true
        )]
)
@kotlinx.serialization.Serializable
data class TemplateData(
    @PrimaryKey(autoGenerate = true)
    var templateId: Int = 0,
    var position: Int = 0,
    var templateGroupId: Int = 0,
    var recipientGroupId: Int? = null,
    var name: String,
    var message: String,
    var iconColor: String = DEFAULT_TEMPLATE_COLOR,
    var favorite: Boolean = false,
    var timestamp: Long = System.currentTimeMillis()
)