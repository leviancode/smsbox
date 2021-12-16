package com.leviancode.android.gsmbox.data.entities.templates.template

import androidx.room.*
import com.leviancode.android.gsmbox.data.entities.recipients.RecipientGroupData
import com.leviancode.android.gsmbox.data.entities.templates.group.TemplateGroupData
import com.leviancode.android.gsmbox.utils.DEFAULT_TEMPLATE_COLOR
import com.leviancode.android.gsmbox.utils.getFormatDate


@Entity(tableName = "templates",
    foreignKeys = [ForeignKey(
        entity = RecipientGroupData::class,
        parentColumns = arrayOf("recipientGroupId"),
        childColumns = arrayOf("recipientGroupId"),
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE,
        deferred = true
    ),
        ForeignKey(
            entity = TemplateGroupData::class,
            parentColumns = arrayOf("templateGroupId"),
            childColumns = arrayOf("templateGroupId"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            deferred = true
        )]
)
@kotlinx.serialization.Serializable
data class TemplateData(
    @PrimaryKey(autoGenerate = true)
    var templateId: Int = 0,
    var templateGroupId: Int = 0,
    var recipientGroupId: Int? = null,
    var name: String,
    var message: String,
    var iconColor: String = DEFAULT_TEMPLATE_COLOR,
    var favorite: Boolean = false,
    val date: String = getFormatDate()
)