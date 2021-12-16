package com.leviancode.android.gsmbox.data.entities.templates.template

import androidx.room.Embedded
import androidx.room.Relation
import com.leviancode.android.gsmbox.data.entities.recipients.GroupWithRecipients
import com.leviancode.android.gsmbox.data.entities.recipients.RecipientGroupData

data class TemplateWithRecipients(
    @Embedded var template: TemplateData,

    @Relation(
        entity = RecipientGroupData::class,
        parentColumn = "recipientGroupId",
        entityColumn = "recipientGroupId"
    )
    var recipients: GroupWithRecipients?
)