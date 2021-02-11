package com.leviancode.android.gsmbox.data.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
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
    fun getRecipientName() = model.name
    fun setRecipientName(value: String){
        if (model.name != value){
            model.name = value
            notifyPropertyChanged(BR.recipientName)
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
    fun isFieldsFilled() = isNotEmpty(getRecipientName(), getPhoneNumber())

}