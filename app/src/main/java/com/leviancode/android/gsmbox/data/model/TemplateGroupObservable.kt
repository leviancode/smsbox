package com.leviancode.android.gsmbox.data.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.leviancode.android.gsmbox.BR

class TemplateGroupObservable : BaseObservable() {
    var data = TemplateGroup()
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    fun getName() = data.name
    fun setName(value: String){
        if (data.name != value){
            data.name = value
            notifyPropertyChanged(BR.name)
        }
    }

    @Bindable
    fun getDescription() = data.description
    fun setDescription(value: String){
        if (data.description != value){
            data.description = value
            notifyPropertyChanged(BR.description)
        }
    }

    @Bindable
    fun getImageUri() = data.imageUri
    fun setImageUri(value: String?){
        if (data.imageUri != value){
            data.imageUri = value
            notifyPropertyChanged(BR.imageUri)
        }
    }

    @Bindable
    fun getIconColor() = data.iconColor
    fun setIconColor(value: Int){
        if (data.iconColor != value){
            data.iconColor = value
            notifyPropertyChanged(BR.iconColor)
        }
    }

    @Bindable
    fun getSize() = data.size

    fun isFieldsNotEmpty(): Boolean {
        return getName().isNotBlank()
                && getDescription().isNotBlank()
                && getImageUri() == null
    }

}