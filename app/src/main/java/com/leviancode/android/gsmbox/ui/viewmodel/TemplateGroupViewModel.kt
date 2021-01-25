package com.leviancode.android.gsmbox.ui.viewmodel

import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.leviancode.android.gsmbox.BR
import com.leviancode.android.gsmbox.data.model.Template
import com.leviancode.android.gsmbox.data.model.TemplateGroup
import com.leviancode.android.gsmbox.repository.TemplatesRepository

class TemplateGroupViewModel : BaseObservable() {
    var group = TemplateGroup()
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    var fold = false

    @Bindable
    fun getName() = group.name
    fun setName(value: String){
        if (group.name != value){
            group.name = value
            notifyChange()
        }
    }

    @Bindable
    fun getDescription() = group.description
    fun setDescription(value: String){
        if (group.description != value){
            group.description = value
            notifyChange()
        }
    }

    @Bindable
    fun getImageUri() = group.imageUri
    fun setImageUri(value: String?){
        if (group.imageUri != value){
            group.imageUri = value
            notifyChange()
        }
    }

    @Bindable
    fun getIconColor() = group.iconColor
    fun setIconColor(value: Int?){
        if (group.iconColor != value){
            group.iconColor = value
            notifyChange()
        }
    }

    @Bindable
    fun getSize() = group.templates.size

    fun unfoldGroup(view: View){
        fold = !fold
        notifyPropertyChanged(BR.fold)
    }

    fun saveTemplate(template: Template){
        group.templates.add(template)
        TemplatesRepository.updateGroup(group)
    }



}