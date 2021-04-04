package com.leviancode.android.gsmbox.data.model.templates

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.utils.isNotEmpty
import java.io.Serializable
import java.util.*


@Entity(tableName = "templates")
@kotlinx.serialization.Serializable
data class Template(
    @PrimaryKey
    var templateId: String = UUID.randomUUID().toString(),
    var templateGroupId: String = "",
    private var name: String = "",
    private var message: String = "",
    private var iconColor: String = "#66BB6A",
    private var favorite: Boolean = false,
    private var recipients: MutableList<Recipient> = mutableListOf(),
    @Embedded
    private var recipientGroup: RecipientGroup? = null
) : BaseObservable(), Serializable {

    @Bindable
    fun getName() = name
    fun setName(value: String) {
        if (name != value) {
            name = value
            notifyChange()
        }
    }

    @Bindable
    fun getMessage() = message
    fun setMessage(value: String) {
        if (message != value) {
            message = value
            notifyChange()
        }
    }

    @Bindable
    fun isFavorite() = favorite
    fun setFavorite(value: Boolean) {
        if (favorite != value) {
            favorite = value
            notifyChange()
        }
    }

    @Bindable
    fun getIconColor() = iconColor
    fun setIconColor(value: String) {
        if (iconColor != value) {
            iconColor = value
            notifyChange()
        }
    }

    @Bindable
    fun getRecipientsAsString(): String {
        return if (isRecipientsNotEmpty()) {
            recipients.joinToString("; ") { it.getPhoneNumber() }
        } else ""
    }

    fun getRecipients() = recipients
    fun setRecipients(value: List<Recipient>) {
        recipients = value.toMutableList()
        notifyChange()
    }

    @Bindable
    fun getRecipientGroupNameWithCount(): String {
        return if (getRecipientGroupName().isNotEmpty()) {
            "${getRecipientGroupName()} (${getRecipientsCount()})"
        } else ""
    }

    fun getRecipientsCount(): Int  = recipients.filter {
            it.getPhoneNumber().isNotBlank()
        }.count()

    @Bindable
    fun getRecipientGroupName() = recipientGroup?.getRecipientGroupName() ?: ""
    fun getRecipientGroupId() = recipientGroup?.recipientGroupId ?: ""

    fun getRecipientGroup() = recipientGroup
    fun setRecipientGroup(value: RecipientGroup?) {
        recipientGroup = value
        notifyChange()
    }

    fun addRecipient(recipient: Recipient) {
        recipients.add(recipient)
        recipientGroup = null
        notifyChange()
    }

    fun removeRecipient(recipient: Recipient) {
        recipients.remove(recipient)
        recipientGroup = null
        notifyChange()
    }

    @Ignore
    @get:Bindable
    var isTemplateNameUnique = true
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    fun isRecipientsNotEmpty() = getRecipientsCount() > 0

    @Bindable
    fun isRecipientGroupAttached() = recipientGroup != null

    fun isRecipientsOrGroup() = isRecipientsNotEmpty() or isRecipientGroupAttached()

    @Bindable
    fun isFieldsCorrect() = isTemplateNameUnique && isNotEmpty(
        getName(),
        getMessage()
    ) && isRecipientsOrGroup()
}
