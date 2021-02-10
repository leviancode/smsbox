package com.leviancode.android.gsmbox.data.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
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
            notifyChange()
        }
    }

    @Bindable
    fun getTemplateGroupDescription() = model.description
    fun setTemplateGroupDescription(value: String){
        if (model.description != value){
            model.description = value
            notifyChange()
        }
    }

    @Bindable
    fun getTemplateGroupIconColor() = model.iconColor
    fun setTemplateGroupIconColor(value: Int){
        if (model.iconColor != value){
            model.iconColor = value
            notifyChange()
        }
    }

    @Bindable
    fun getTemplateGroupImageUri() = model.imageUri
    fun setTemplateGroupImageUri(value: String){
        if (model.imageUri != value){
            model.imageUri = value
            notifyChange()
        }
    }

    @Bindable
    fun getTemplateGroupSize() = model.size.toString()

    @Bindable
    fun isRequiredFieldsFilled() = isNotEmpty(getTemplateGroupName())

    fun isAllFieldsFilled() = isNotEmpty(getTemplateGroupName(), getTemplateGroupDescription())
}