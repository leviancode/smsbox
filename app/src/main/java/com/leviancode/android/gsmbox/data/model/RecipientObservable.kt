package com.leviancode.android.gsmbox.data.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

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
                notifyChange()
            }
        }

    @Bindable
    fun getName() = data.name
    fun setName(value: String){
        if (data.name != value){
            data.name = value
            notifyChange()
        }
    }

    @Bindable
    fun getPhoneNumber() = data.phoneNumber
    fun setPhoneNumber(value: String){
        if (data.phoneNumber != value){
            data.phoneNumber = value
            notifyChange()
        }
    }

    fun isFieldsNotEmpty(): Boolean {
        return getName().isNotBlank() && getPhoneNumber().isNotBlank()
    }

}