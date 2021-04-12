package com.leviancode.android.gsmbox.data.model.templates

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Embedded
import androidx.room.Relation
import com.leviancode.android.gsmbox.data.model.recipients.GroupWithRecipients
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup

data class TemplateWithRecipients(
    @Bindable
    @Embedded var template: Template,

    @Relation(
        entity = RecipientGroup::class,
        parentColumn = "recipientGroupId",
        entityColumn = "recipientGroupId"
    )
    @Bindable
    var recipients: GroupWithRecipients
): BaseObservable() {

    @Bindable
    fun isFieldsFilledAndCorrect() = template.isFieldsCorrect() && recipients.isRecipientsNotEmpty()

    fun addRecipient(recipient: Recipient) {
        recipients.addRecipient(recipient)
        clearGroup()
        notifyChange()
    }

    fun removeRecipient(recipient: Recipient) {
        recipients.removeRecipient(recipient)
        clearGroup()
        notifyChange()
    }

    private fun clearGroup() {
        if (recipients.group.isNameNotNullOrEmpty()){
            recipients.group = RecipientGroup().also {
                template.recipientGroupId = it.recipientGroupId
            }
        }
    }
}