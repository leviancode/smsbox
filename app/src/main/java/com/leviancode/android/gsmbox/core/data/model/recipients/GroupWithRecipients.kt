package com.leviancode.android.gsmbox.core.data.model.recipients

import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.leviancode.android.gsmbox.R
import java.lang.StringBuilder

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

    /*@Bindable
    fun getRecipientsAsString(): String {
        return if (isRecipientsNotEmpty()) {
            recipients.joinToString("; ") { it.getPhoneNumber() }
        } else ""
    }*/

    @Bindable
    fun getRecipientsAsString(): String {
        val stringBuilder = StringBuilder()
        recipients.forEach { recipient ->
            if (!recipient.getName().isNullOrBlank()){
                stringBuilder.append("${recipient.getName()} (${recipient.getPhoneNumber()});\n")
            } else {
                stringBuilder.append(recipient.getPhoneNumber() + ";\n")
            }
        }
        return stringBuilder.toString().trim()
    }

    fun getFormatRecipients(context: Context): String {
       return when {
           recipients.isEmpty() -> {
               context.getString(R.string.no_recipients)
           }
           isGroupNameNotNull() -> {
               "${context.getString(R.string.group)} ${getRecipientGroupName()} (${getRecipientsCount()})"
           }
           else -> {
               getRecipientsAsString()
           }
       }
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

    @Bindable
    fun isHasRecipientsOrGroup() = getRecipientsCount() > 0 || group.isNameNotNullOrEmpty()

    fun copy() = GroupWithRecipients(
        group = this.group.copy(),
        recipients = this.recipients.map { it.copy() }.toMutableList()
    )

}
