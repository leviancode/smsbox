package com.leviancode.android.gsmbox.ui.entities.recipients

import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.leviancode.android.gsmbox.BR
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.ui.base.BaseEntity
import com.leviancode.android.gsmbox.utils.DEFAULT_GROUP_COLOR

data class RecipientGroupUI(
    override var id: Int = 0,
    override var position: Int = 0,
    private var name: String? = null,
    private var iconColor: String = DEFAULT_GROUP_COLOR,
    val recipients: MutableList<RecipientUI> = mutableListOf(),
    val timestamp: Long = System.currentTimeMillis()
): BaseObservable(), BaseEntity {

    fun updateGroup(newGroup: RecipientGroupUI){
        id = newGroup.id
        setName(newGroup.getName())
        setIconColor(newGroup.getIconColor())
        updateRecipients(newGroup.recipients)
    }

    @Bindable
    fun getName() = name
    fun setName(value: String?){
        if (value != name){
            name = value
            notifyPropertyChanged(BR.name)
        }
    }

    @Bindable
    fun getIconColor() = iconColor
    fun setIconColor(value: String){
        if (value != iconColor){
            iconColor = value
            notifyPropertyChanged(BR.iconColor)
        }
    }

    @Bindable
    fun getSizeAsString() = getRecipientsCount().toString()

    fun getFormatRecipients(context: Context): String {
        return when {
            recipients.isEmpty() -> {
                context.getString(R.string.no_recipients)
            }
            isGroupNameNotNull() -> {
                "${context.getString(R.string.group)} $name (${getRecipientsCount()})"
            }
            else -> {
                getRecipientsAsString()
            }
        }
    }

    private fun getRecipientsAsString(): String {
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

    fun getRecipientsCount(): Int = recipients.count {
        it.getPhoneNumber().isNotBlank()
    }

    fun updateRecipients(value: List<RecipientUI>) {
        recipients.clear()
        recipients.addAll(value)
        notifyChange()
    }

    fun addRecipient(recipient: RecipientUI) {
        recipients.add(recipient)
    }

    fun removeRecipient(recipient: RecipientUI) {
        recipients.remove(recipient)
    }

    @Bindable
    fun isGroupNameNotNull() = !name.isNullOrBlank()

    @Bindable
    fun getRecipientGroupNameWithCount(): String {
        return if (isGroupNameNotNull()) {
            "$name (${getRecipientsCount()})"
        } else ""
    }

    @Bindable
    var selected: Boolean = false
        set(value) {
            if (field != value){
                field = value
                notifyPropertyChanged(BR.selected)
            }
        }

    @Bindable
    fun isRecipientsNotEmpty() = getRecipientsCount() > 0

    @Bindable
    fun isHasRecipientsOrGroup() = isRecipientsNotEmpty() || !name.isNullOrBlank()

    fun copy() = this.copy(
        recipients = this.recipients.map { it.copy() }.toMutableList()
    )
}