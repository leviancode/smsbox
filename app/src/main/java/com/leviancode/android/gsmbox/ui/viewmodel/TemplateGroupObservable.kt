package com.leviancode.android.gsmbox.ui.viewmodel

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.leviancode.android.gsmbox.BR
import com.leviancode.android.gsmbox.data.model.Template
import com.leviancode.android.gsmbox.data.model.TemplateGroup
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository

class TemplateGroupObservable : BaseObservable() {
    var group = TemplateGroup()
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    fun getName() = group.name
    fun setName(value: String){
        if (group.name != value){
            group.name = value
            notifyPropertyChanged(BR.name)
        }
    }

    @Bindable
    fun getDescription() = group.description
    fun setDescription(value: String){
        if (group.description != value){
            group.description = value
            notifyPropertyChanged(BR.description)
        }
    }

    @Bindable
    fun getImageUri() = group.imageUri
    fun setImageUri(value: String?){
        if (group.imageUri != value){
            group.imageUri = value
            notifyPropertyChanged(BR.imageUri)
        }
    }

    @Bindable
    fun getIconColor() = group.iconColor
    fun setIconColor(value: Int){
        if (group.iconColor != value){
            group.iconColor = value
            notifyPropertyChanged(BR.iconColor)
        }
    }

    @Bindable
    fun getSize() = group.templates.size

    fun isFieldsEmpty(): Boolean {
        return getName().isBlank()
                && getDescription().isBlank()
                && getImageUri() == null
    }

}