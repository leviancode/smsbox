package com.leviancode.android.gsmbox.data.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class RecipientObservable : BaseObservable() {
    var recipient = Recipient()
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    fun getName() = recipient.name
    fun setName(value: String){
        if (recipient.name != value){
            recipient.name = value
            notifyChange()
        }
    }

    @Bindable
    fun getPhoneNumber() = recipient.phoneNumber
    fun setPhoneNumber(value: String){
        if (recipient.phoneNumber != value){
            recipient.phoneNumber = value
            notifyChange()
        }
    }

    fun isFieldsNotEmpty(): Boolean {
        return getName().isNotBlank() && getPhoneNumber().isNotBlank()
    }

}