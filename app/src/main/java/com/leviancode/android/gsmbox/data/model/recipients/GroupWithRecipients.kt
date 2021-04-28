package com.leviancode.android.gsmbox.data.model.recipients

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class GroupWithRecipients(
    @Embedded var group: RecipientGroup = RecipientGroup(),
    @Relation(
        parentColumn = "recipientGroupId",
        entityColumn = "recipientId",
        associateBy = Junction(RecipientsAndGroupRelation::class)
    )
    private var recipients: MutableList<Recipient> = mutableListOf()
) : BaseObservable() {

    @Bindable
    fun getSizeAsString() = getRecipientsCount().toString()

    @Bindable
    fun getRecipientsAsString(): String {
        return if (isRecipientsNotEmpty()) {
            recipients.joinToString("; ") { it.getPhoneNumber() }
        } else ""
    }

    fun getRecipientsCount(): Int = recipients.filter {
        it.getPhoneNumber().isNotBlank()
    }.count()

    fun getRecipients(): List<Recipient> = recipients
    fun setRecipients(value: List<Recipient>) {
        recipients = value.toMutableList()
        notifyChange()
    }

    fun addRecipient(recipient: Recipient) {
        recipients.add(recipient)
        notifyChange()
    }

    fun removeRecipient(recipient: Recipient) {
        recipients.remove(recipient)
        notifyChange()
    }

    @Bindable
    fun getRecipientGroupName() = group.getName()

    @Bindable
    fun isGroupNameNotNull() = group.isNameNotNullOrEmpty()

    @Bindable
    fun getRecipientGroupNameWithCount(): String {
        return if (isGroupNameNotNull()) {
            "${getRecipientGroupName()} (${getRecipientsCount()})"
        } else ""
    }

    @Bindable
    fun isRecipientsNotEmpty() = getRecipientsCount() > 0

    fun copy() = GroupWithRecipients(
        group = this.group.copy(),
        recipients = this.recipients.map { it.copy() }.toMutableList()
    )

}
