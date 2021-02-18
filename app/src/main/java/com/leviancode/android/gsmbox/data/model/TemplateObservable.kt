package com.leviancode.android.gsmbox.data.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.leviancode.android.gsmbox.BR
import com.leviancode.android.gsmbox.utils.isNotEmpty

class TemplateObservable() : BaseObservable() {
    constructor(model: Template) : this() {
        this.model = model
    }
    var model = Template()
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    fun getName() = model.name
    fun setName(value: String){
        if (model.name != value){
            model.name = value
            notifyPropertyChanged(BR.name)
        }
    }

    @Bindable
    fun getMessage() = model.message
    fun setMessage(value: String){
        if (model.message != value){
            model.message = value
            notifyPropertyChanged(BR.message)
        }
    }

    @Bindable
    fun isFavorite() = model.favorite
    fun setFavorite(value: Boolean){
        if (model.favorite != value){
            model.favorite = value
            notifyPropertyChanged(BR.favorite)
        }
    }

    @Bindable
    fun getIconColor() = model.iconColor
    fun setIconColor(value: Int){
        if (model.iconColor != value){
            model.iconColor = value
            notifyPropertyChanged(BR.iconColor)
        }
    }

    @Bindable
    fun getRecipientsAsString() = model.recipients.joinToString("; "){it.phoneNumber}

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
    fun isFieldsFilled() = isNotEmpty(getName(), getMessage(), getRecipientsAsString())
}