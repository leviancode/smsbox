package com.leviancode.android.gsmbox.ui.entities.recipients

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import com.leviancode.android.gsmbox.BR
import com.leviancode.android.gsmbox.ui.base.BaseEntity

data class RecipientUI(
    override var id: Int = 0,
    override var position: Int = 0,
    private var name: String? = null,
    private var phoneNumber: String = "",
    val timestamp: Long = System.currentTimeMillis()
): BaseObservable(), BaseEntity {

    @Bindable
    fun getName() = name
    fun setName(value: String?){
        if (value != name){
            name = value
            notifyPropertyChanged(BR.name)
            notifyPropertyChanged(BR.fieldsValid)
        }
    }

    @Bindable
    fun getPhoneNumber() = phoneNumber
    fun setPhoneNumber(value: String){
        if (value != phoneNumber){
            phoneNumber = value
            notifyPropertyChanged(BR.phoneNumber)
            notifyPropertyChanged(BR.fieldsValid)
        }
    }

    @Bindable
    var phoneNumberUnique: Boolean = true
        set(value) {
            if (value != field){
                field = value
                notifyPropertyChanged(BR.fieldsValid)
                notifyPropertyChanged(BR.phoneNumberUnique)
            }
        }

    var nameUnique: Boolean = true
        set(value) {
            if (value != field){
                field = value
                notifyPropertyChanged(BR.fieldsValid)
            }
        }

    @get:Bindable
    val fieldsValid: Boolean
        get() = !name.isNullOrBlank() && phoneNumber.isNotBlank() && phoneNumberUnique && nameUnique

    val selected = ObservableBoolean(false)
}
