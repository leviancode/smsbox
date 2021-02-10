package com.leviancode.android.gsmbox.data.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.leviancode.android.gsmbox.utils.isNotEmpty

class TemplateObservable : BaseObservable() {
    var model = Template()
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    fun isFavorite() = model.favorite
    fun setFavorite(value: Boolean){
        if (model.favorite != value){
            model.favorite = value
            notifyChange()
        }
    }

    @Bindable
    fun getTemplateName() = model.name
    fun setTemplateName(value: String){
        if (model.name != value){
            model.name = value
            notifyChange()
        }
    }

    @Bindable
    fun getMessage() = model.message
    fun setMessage(value: String){
        if (model.message != value){
            model.message = value
            notifyChange()
        }
    }

    @Bindable
    fun getTemplateIconColor() = model.iconColor
    fun setTemplateIconColor(value: Int){
        if (model.iconColor != value){
            model.iconColor = value
            notifyChange()
        }
    }

    @Bindable
    fun getRecipientsAsString() = model.recipients.joinToString(", "){it.phoneNumber}

    fun getRecipients() = model.recipients
    fun setRecipients(value: MutableList<Recipient>){
        model.recipients = value
        notifyChange()
    }

    fun addRecipient(recipient: Recipient){
        model.recipients.add(recipient)
    }

    fun removeRecipient(recipient: Recipient){
        model.recipients.remove(recipient)
    }

    @Bindable
    fun isFieldsFilled() = isNotEmpty(getTemplateName(), getMessage(), getRecipientsAsString())

    fun setGroupId(value: String){
        model.groupId = value
    }
}