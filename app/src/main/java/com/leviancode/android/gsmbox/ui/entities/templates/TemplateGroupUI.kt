package com.leviancode.android.gsmbox.ui.entities.templates

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.leviancode.android.gsmbox.BR
import com.leviancode.android.gsmbox.ui.base.BaseEntity
import com.leviancode.android.gsmbox.utils.DEFAULT_GROUP_COLOR

data class TemplateGroupUI(
    override var id: Int = 0,
    override var position: Int = 0,
    private var name: String = "",
    private var description: String = "",
    private var iconColor: String = DEFAULT_GROUP_COLOR,
    var size: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
): BaseObservable(), BaseEntity {

    val sizeAsString: String
        get() = size.toString()

    @Bindable
    fun getName() = name
    fun setName(value: String){
        if (value != name){
            name = value
            notifyPropertyChanged(BR.name)
            notifyPropertyChanged(BR.nameUniqueAndNotBlank)
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

    var nameUnique = true
        set(value) {
            if (value != field){
                field = value
                notifyPropertyChanged(BR.nameUniqueAndNotBlank)
            }
        }

    @get:Bindable
    val nameUniqueAndNotBlank: Boolean
        get() = name.isNotBlank() && nameUnique
}
