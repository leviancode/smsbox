package com.leviancode.android.gsmbox.data.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

class RecipientObservable : BaseObservable() {
    var data = Recipient()
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    fun getRecipientName() = data.name
    fun setRecipientName(value: String){
        if (data.name != value){
            data.name = value
            notifyPropertyChanged(BR.recipientName)
        }
    }

    @Bindable
    fun getPhoneNumber() = data.phoneNumber
    fun setPhoneNumber(value: String){
        if (data.phoneNumber != value){
            data.phoneNumber = value
            notifyPropertyChanged(BR.phoneNumber)
        }
    }

    fun isFieldsNotEmpty(): Boolean {
        return getRecipientName().isNotBlank() && getPhoneNumber().isNotBlank()
    }

}