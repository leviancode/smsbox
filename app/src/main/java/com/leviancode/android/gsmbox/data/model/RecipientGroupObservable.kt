package com.leviancode.android.gsmbox.data.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.leviancode.android.gsmbox.BR
import com.leviancode.android.gsmbox.utils.isNotEmpty

class RecipientGroupObservable() : BaseObservable(){
    constructor(model: RecipientGroup) : this() {
        this.model = model
    }
    var model = RecipientGroup()
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    var selected = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.selected)
        }

    @Bindable
    fun getName() = model.groupName
    fun setName(value: String){
        if (model.groupName != value){
            model.groupName = value
            notifyPropertyChanged(BR.name)
            notifyPropertyChanged(BR.fieldsFilled)
        }
    }

    /*@Bindable
    fun getDescription() = model.description
    fun setDescription(value: String){
        if (model.description != value){
            model.description = value
            notifyPropertyChanged(BR.description)
        }
    }*/

    @Bindable
    fun getIconColor() = model.groupIconColor
    fun setIconColor(value: Int){
        if (model.groupIconColor != value){
            model.groupIconColor = value
            notifyPropertyChanged(BR.iconColor)
        }
    }

    @Bindable
    fun getSizeAsString() = model.size.toString()

    fun getSize() = model.size

    @Bindable
    fun isFieldsFilled() = isNotEmpty(getName())

    fun getGroupId() = model.groupId

}