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
    fun getTemplateGroupName() = data.name
    fun setTemplateGroupName(value: String){
        if (data.name != value){
            data.name = value
            notifyPropertyChanged(BR.templateGroupName)
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
    fun getTemplateGroupImageUri() = data.imageUri
    fun setTemplateGroupImageUri(value: String?){
        if (data.imageUri != value){
            data.imageUri = value
            notifyPropertyChanged(BR.templateGroupImageUri)
        }
    }

    @Bindable
    fun getTemplateGroupIconColor() = data.iconColor
    fun setTemplateGroupIconColor(value: Int){
        if (data.iconColor != value){
            data.iconColor = value
            notifyPropertyChanged(BR.templateGroupIconColor)
        }
    }

    @Bindable
    fun getSize() = data.size

    fun isFieldsNotEmpty(): Boolean {
        return getTemplateGroupName().isNotBlank()
                && getDescription().isNotBlank()
                && getTemplateGroupImageUri() == null
    }

}