package com.leviancode.android.gsmbox.ui.viewmodel

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.leviancode.android.gsmbox.BR
import com.leviancode.android.gsmbox.data.model.Recipient
import com.leviancode.android.gsmbox.data.model.Template

class TemplateObservable : BaseObservable() {
    var template = Template()
        set(value) {
            field = value
            notifyChange()
        }

    fun setGroupId(value: String){
        template.groupId = value
    }

    @Bindable
    fun isFavorite() = template.favorite
    fun setFavorite(value: Boolean){
        if (template.favorite != value){
            template.favorite = value
            notifyPropertyChanged(BR.favorite)
        }
    }

    @Bindable
    fun getName() = template.name
    fun setName(value: String){
        if (template.name != value){
            template.name = value
            notifyChange()
        }
    }

    @Bindable
    fun getMessage() = template.message
    fun setMessage(value: String){
        if (template.message != value){
            template.message = value
            notifyChange()
        }
    }

    fun getRecipient() = template.recipients

    @Bindable
    fun getIconColor() = template.iconColor
    fun setIconColor(value: Int){
        if (template.iconColor != value){
            template.iconColor = value
            notifyPropertyChanged(BR.iconColor)
        }
    }

    fun onFavoriteClicked(value: Boolean){
       setFavorite(!value)
    }

    fun addRecipient(number: String){
        template.recipients.add(Recipient(phoneNumber = number))
    }

    fun isFieldsEmpty(): Boolean {
        return getName().isBlank()
                && getMessage().isBlank()
               // && getRecipient().isBlank()
    }


}