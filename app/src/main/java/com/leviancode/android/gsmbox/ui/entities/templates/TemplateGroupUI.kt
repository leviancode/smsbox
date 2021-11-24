package com.leviancode.android.gsmbox.ui.entities.templates

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.leviancode.android.gsmbox.BR
import com.leviancode.android.gsmbox.ui.base.BaseEntity
import com.leviancode.android.gsmbox.utils.DEFAULT_GROUP_COLOR

data class TemplateGroupUI(
    override var id: Int = 0,
    private var name: String = "",
    private var description: String = "",
    private var iconColor: String = DEFAULT_GROUP_COLOR,
    @Bindable var size: Int = 0
): BaseObservable(), BaseEntity {

    @Bindable
    fun getName() = name
    fun setName(value: String){
        if (value != name){
            name = value
            notifyPropertyChanged(BR.name)
        }
    }

    @Bindable
    fun getDescription() = description
    fun setDescription(value: String){
        if (value != description){
            description = value
            notifyPropertyChanged(BR.description)
        }
    }

    @Bindable
    fun getIconColor() = iconColor
    fun setIconColor(value: String){
        if (value != iconColor){
            iconColor = value
            notifyPropertyChanged(BR.iconColor)
        }
    }
}
