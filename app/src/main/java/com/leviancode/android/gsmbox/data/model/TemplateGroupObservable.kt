package com.leviancode.android.gsmbox.data.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.leviancode.android.gsmbox.BR
import com.leviancode.android.gsmbox.utils.isNotEmpty

class TemplateGroupObservable() : BaseObservable() {
    constructor(model: TemplateGroup) : this(){
        this.model = model
    }
    var model = TemplateGroup()
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
            notifyPropertyChanged(BR.requiredFieldsFilled)
        }
    }

    @Bindable
    fun getDescription() = model.description
    fun setDescription(value: String){
        if (model.description != value){
            model.description = value
            notifyPropertyChanged(BR.description)
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
    fun getImageUri() = model.imageUri
    fun setImageUri(value: String){
        if (model.imageUri != value){
            model.imageUri = value
            notifyPropertyChanged(BR.imageUri)
        }
    }

    @Bindable
    fun getSize() = model.size.toString()

    @Bindable
    fun isRequiredFieldsFilled() = isNotEmpty(getName())

    fun isAllFieldsFilled() = isNotEmpty(getName(), getDescription())
}