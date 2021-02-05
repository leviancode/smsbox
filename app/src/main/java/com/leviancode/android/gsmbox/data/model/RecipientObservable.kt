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

    @get:Bindable
    var saved = false
        set(value){
            if (field != value){
                field = value
                notifyPropertyChanged(BR.saved)
            }
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