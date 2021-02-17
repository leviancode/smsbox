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
    fun getName() = model.name
    fun setName(value: String){
        if (model.name != value){
            model.name = value
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
    fun getIconColor() = model.iconColor
    fun setIconColor(value: Int){
        if (model.iconColor != value){
            model.iconColor = value
            notifyPropertyChanged(BR.iconColor)
        }
    }

    @Bindable
    fun getSize() = model.size.toString()

    @Bindable
    fun isFieldsFilled() = isNotEmpty(getName())

    fun getGroupId() = model.groupId

}