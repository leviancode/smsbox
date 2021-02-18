package com.leviancode.android.gsmbox.data.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.leviancode.android.gsmbox.BR
import com.leviancode.android.gsmbox.utils.isNotEmpty

class RecipientObservable() : BaseObservable(){
    constructor(model: Recipient) : this() {
        this.model = model
    }
    var model = Recipient()
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    fun getName() = model.recipientName
    fun setName(value: String){
        if (model.recipientName != value){
            model.recipientName = value
            notifyPropertyChanged(BR.name)
            notifyPropertyChanged(BR.fieldsFilled)
        }
    }

    @Bindable
    fun getPhoneNumber() = model.phoneNumber
    fun setPhoneNumber(value: String){
        if (model.phoneNumber != value){
            model.phoneNumber = value
            notifyPropertyChanged(BR.phoneNumber)
            notifyPropertyChanged(BR.fieldsFilled)
        }
    }

    @Bindable
    fun getGroupName() = model.groupName ?: ""
    fun setGroupName(value: String?){
        if (model.groupName != value){
            model.groupName = value
            notifyPropertyChanged(BR.groupName)
        }
    }

    @Bindable
    fun isFieldsFilled() = isNotEmpty(getName(), getPhoneNumber())
}