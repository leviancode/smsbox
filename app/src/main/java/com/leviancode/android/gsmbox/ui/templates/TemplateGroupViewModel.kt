package com.leviancode.android.gsmbox.ui.templates

import android.graphics.Bitmap
import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.leviancode.android.gsmbox.BR
import com.leviancode.android.gsmbox.model.Template
import com.leviancode.android.gsmbox.model.TemplateGroup
import com.leviancode.android.gsmbox.repository.TemplatesRepository

class TemplateGroupViewModel : BaseObservable() {
    var group: TemplateGroup? = null
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    var isFold = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.fold)
        }

    @get:Bindable
    var groupName = ""
        get() = group?.name ?: ""
        set(value) {
            group?.name = value
            field = value
            updateGroup()
        }

    @get:Bindable
    var groupDescription = ""
        get() = group?.description ?: ""
        set(value) {
            group?.description = value
            field = value
            updateGroup()
        }

    @get:Bindable
    var groupSize: Int = 0
        get() = group?.size ?: 0
        private set

    @get:Bindable
    var groupImage: Bitmap? = null
        get() = group?.image
        set(value) {
            group?.image = value
            field = value
            updateGroup()
        }

    @get:Bindable
    var imageUri: String? = null
        get() = group?.imageUri
        set(value) {
            group?.imageUri = value
            field = value
            updateGroup()
        }

    @get:Bindable
    var groupIconColor: Int? = null
        get() = group?.iconColor
        set(value) {
            group?.iconColor = value
            field = value
            updateGroup()
        }

    private fun updateGroup(){
        TemplatesRepository.updateGroup(group!!)
        notifyChange()
    }

    fun addTemplate(item: Template){
        group?.add(item)
        updateGroup()
    }

    fun onFold(view: View){
        isFold = !isFold
    }
}