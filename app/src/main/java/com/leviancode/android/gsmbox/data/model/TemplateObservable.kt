package com.leviancode.android.gsmbox.data.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.leviancode.android.gsmbox.BR

class TemplateObservable : BaseObservable() {
    var data = Template()
        set(value) {
            field = value
            notifyChange()
        }

    fun setGroupId(value: String){
        data.groupId = value
    }

    @Bindable
    fun isFavorite() = data.favorite
    fun setFavorite(value: Boolean){
        if (data.favorite != value){
            data.favorite = value
            notifyPropertyChanged(BR.favorite)
        }
    }

    @Bindable
    fun getTemplateName() = data.name
    fun setTemplateName(value: String){
        if (data.name != value){
            data.name = value
            notifyPropertyChanged(BR.templateName)
        }
    }

    @Bindable
    fun getMessage() = data.message
    fun setMessage(value: String){
        if (data.message != value){
            data.message = value
            notifyPropertyChanged(BR.message)
        }
    }
    @Bindable
    fun getRecipientsAsString() = data.recipients.joinToString(", ")

    fun getRecipients() = data.recipients
    fun setRecipients(value: MutableList<String>){
        data.recipients = value
    }

    @Bindable
    fun getTemplateIconColor() = data.iconColor
    fun setTemplateIconColor(value: Int){
        if (data.iconColor != value){
            data.iconColor = value
           notifyPropertyChanged(BR.templateIconColor)
        }
    }

    fun isFieldsNotEmpty(): Boolean {
        return getTemplateName().isNotBlank()
                && getMessage().isNotBlank()
    }


}