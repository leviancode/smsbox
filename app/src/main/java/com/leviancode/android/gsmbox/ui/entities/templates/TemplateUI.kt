package com.leviancode.android.gsmbox.ui.entities.templates

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.leviancode.android.gsmbox.BR
import com.leviancode.android.gsmbox.ui.base.BaseEntity
import com.leviancode.android.gsmbox.utils.DEFAULT_TEMPLATE_COLOR
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientGroupUI

class TemplateUI(
    override var id: Int = 0,
    var groupId: Int = 0,
    private var name: String = "",
    private var message: String = "",
    private var iconColor: String = DEFAULT_TEMPLATE_COLOR,
    private var favorite: Boolean = false,
    var recipientGroup: RecipientGroupUI = RecipientGroupUI()
) : BaseObservable(), BaseEntity {

    @Bindable
    fun getName() = name
    fun setName(value: String){
        if (value != name){
            name = value
            notifyPropertyChanged(BR.name)
            notifyPropertyChanged(BR.nameAndMessageNotEmpty)
        }
    }

    @Bindable
    fun getMessage() = message
    fun setMessage(value: String){
        if (value != message){
            message = value
            notifyPropertyChanged(BR.message)
            notifyPropertyChanged(BR.nameAndMessageNotEmpty)
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

    @Bindable
    fun isFavorite() = favorite
    fun setFavorite(value: Boolean){
        if (value != favorite){
            favorite = value
            notifyPropertyChanged(BR.favorite)
        }
    }

    @get:Bindable
    val isNameAndMessageNotEmpty: Boolean
        get() = name.isNotEmpty() && message.isNotEmpty()

    fun copy() = TemplateUI(
        id,
        groupId,
        name,
        message,
        iconColor,
        favorite,
        recipientGroup.copy()
    )
}