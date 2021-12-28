package com.leviancode.android.gsmbox.ui.entities.placeholder

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.leviancode.android.gsmbox.BR
import com.leviancode.android.gsmbox.ui.base.BaseEntity

data class PlaceholderUI(
    override var id: Int = 0,
    override var position: Int = 0,
    private var name: String = "",
    private var value: String = "",
    val timestamp: Long = System.currentTimeMillis()
): BaseObservable(), BaseEntity{

    val nameWithHashTag: String
        get() = "#$name"

    var nameUnique: Boolean = true
        set(value) {
            field = value
            notifyPropertyChanged(BR.fieldsValid)
        }

    @Bindable
    fun getName() = name
    fun setName(value: String){
        if (value != name){
            name = value
            notifyPropertyChanged(BR.name)
            notifyPropertyChanged(BR.fieldsValid)
        }
    }

    @Bindable
    fun getValue() = value
    fun setValue(value: String){
        if (this.value != value){
            this.value = value
            notifyPropertyChanged(BR.value)
            notifyPropertyChanged(BR.fieldsValid)
        }
    }

    @get:Bindable
    val fieldsValid: Boolean
        get() = name.isNotBlank() && value.isNotBlank() && nameUnique
}
