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
    fun getName() = data.name
    fun setName(value: String){
        if (data.name != value){
            data.name = value
            notifyChange()
        }
    }

    @Bindable
    fun getMessage() = data.message
    fun setMessage(value: String){
        if (data.message != value){
            data.message = value
            notifyChange()
        }
    }

    fun getRecipients() = data.recipients

    fun setRecipients(value: MutableList<Recipient>){
        data.recipients = value
    }

    @Bindable
    fun getIconColor() = data.iconColor
    fun setIconColor(value: Int){
        if (data.iconColor != value){
            data.iconColor = value
            notifyPropertyChanged(BR.iconColor)
        }
    }

    fun onFavoriteClicked(value: Boolean){
       setFavorite(!value)
    }


    fun isFieldsNotEmpty(): Boolean {
        return getName().isNotBlank()
                && getMessage().isNotBlank()
    }


}