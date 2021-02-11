package com.leviancode.android.gsmbox.data.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.leviancode.android.gsmbox.utils.isNotEmpty

class TemplateGroupObservable : BaseObservable() {
    var model = TemplateGroup()
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    fun getTemplateGroupName() = model.name
    fun setTemplateGroupName(value: String){
        if (model.name != value){
            model.name = value
            notifyPropertyChanged(BR.templateGroupName)
            notifyPropertyChanged(BR.requiredFieldsFilled)
        }
    }

    @Bindable
    fun getTemplateGroupDescription() = model.description
    fun setTemplateGroupDescription(value: String){
        if (model.description != value){
            model.description = value
            notifyPropertyChanged(BR.templateGroupDescription)
        }
    }

    @Bindable
    fun getTemplateGroupIconColor() = model.iconColor
    fun setTemplateGroupIconColor(value: Int){
        if (model.iconColor != value){
            model.iconColor = value
            notifyPropertyChanged(BR.templateGroupIconColor)
        }
    }

    @Bindable
    fun getTemplateGroupImageUri() = model.imageUri
    fun setTemplateGroupImageUri(value: String){
        if (model.imageUri != value){
            model.imageUri = value
            notifyPropertyChanged(BR.templateGroupImageUri)
        }
    }

    @Bindable
    fun getTemplateGroupSize() = model.size.toString()

    @Bindable
    fun isRequiredFieldsFilled() = isNotEmpty(getTemplateGroupName())

    fun isAllFieldsFilled() = isNotEmpty(getTemplateGroupName(), getTemplateGroupDescription())
}