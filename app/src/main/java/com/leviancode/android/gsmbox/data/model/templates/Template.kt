package com.leviancode.android.gsmbox.data.model.templates

import android.graphics.Color
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.leviancode.android.gsmbox.BR
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
    private var iconColor: Int = Color.parseColor("#66BB6A"),
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
            notifyPropertyChanged(BR.name)
        }
    }

    @Bindable
    fun getMessage() = message
    fun setMessage(value: String) {
        if (message != value) {
            message = value
            notifyPropertyChanged(BR.message)
        }
    }

    @Bindable
    fun isFavorite() = favorite
    fun setFavorite(value: Boolean) {
        if (favorite != value) {
            favorite = value
            notifyPropertyChanged(BR.favorite)
        }
    }

    @Bindable
    fun getIconColor() = iconColor
    fun setIconColor(value: Int) {
        if (iconColor != value) {
            iconColor = value
            notifyPropertyChanged(BR.iconColor)
        }
    }

    @Bindable
    fun getRecipientsAsString() = recipients.joinToString("; ") { it.getPhoneNumber() }

    fun getRecipients() = recipients
    fun setRecipients(value: List<Recipient>) {
        recipients = value.toMutableList()
        notifyChange()
    }

    @Bindable
    fun getRecipientGroupNameWithCount(): String {
        return if (getRecipientGroupName().isNotEmpty()) "${getRecipientGroupName()} (${recipients.size})" else ""
    }

    @Bindable
    fun getRecipientGroupName() = recipientGroup?.getRecipientGroupName() ?: ""
    fun getRecipientGroupId() = recipientGroup?.recipientGroupId ?: ""

    fun getRecipientGroup() = recipientGroup
    fun setRecipientGroup(value: RecipientGroup) {
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

    @Bindable
    fun isFieldsFilled() = isNotEmpty(
        getName(),
        getMessage(),
        getRecipientsAsString() + getRecipientGroupName()
    )
}
