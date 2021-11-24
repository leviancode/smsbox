package com.leviancode.android.gsmbox.ui.entities.recipients

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import com.leviancode.android.gsmbox.BR
import com.leviancode.android.gsmbox.ui.base.BaseEntity

data class RecipientUI(
    override var id: Int = 0,
    private var name: String? = null,
    private var phoneNumber: String = "",
): BaseObservable(), BaseEntity {

    @Bindable
    fun getName() = name
    fun setName(value: String?){
        if (value != name){
            name = value
            notifyPropertyChanged(BR.name)
        }
    }

    @Bindable
    fun getPhoneNumber() = phoneNumber
    fun setPhoneNumber(value: String){
        if (value != phoneNumber){
            phoneNumber = value
            notifyPropertyChanged(BR.phoneNumber)
        }
    }

    @Bindable
    var phoneNumberUnique: Boolean = true
        set(value) {
            if (value != field){
                field = value
                notifyPropertyChanged(BR.phoneNumberUnique)
            }
        }

    val selected = ObservableBoolean(false)
}
