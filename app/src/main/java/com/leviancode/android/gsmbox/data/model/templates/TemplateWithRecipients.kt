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
    var recipients: GroupWithRecipients?
): BaseObservable() {

    @Bindable
    fun isFieldsFilledAndCorrect() = template.isFieldsCorrect() && recipients?.isRecipientsNotEmpty() ?: false

    fun addRecipient(recipient: Recipient) {
        recipients?.addRecipient(recipient)
        clearGroup()
        notifyChange()
    }

    fun removeRecipient(recipient: Recipient) {
        recipients?.removeRecipient(recipient)
        clearGroup()
        notifyChange()
    }

    private fun clearGroup() {
        recipients?.let {
            if (it.group.isNameNotNullOrEmpty()){
                it.group = RecipientGroup()
                template.recipientGroupId = null
            }
        }
    }

    fun copy() = TemplateWithRecipients(
        template = this.template.copy(),
        recipients = this.recipients?.copy()
    )
}