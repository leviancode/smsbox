package com.leviancode.android.gsmbox.data.model.templates

import android.graphics.Color
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.leviancode.android.gsmbox.BR
import com.leviancode.android.gsmbox.utils.isNotEmpty
import java.io.Serializable
import java.util.*

@Entity(tableName = "template_groups")
@kotlinx.serialization.Serializable
data class  TemplateGroup (
    @PrimaryKey
    var templateGroupId: String = UUID.randomUUID().toString(),
    private var name: String = "",
    private var description: String = "",
    private var imageUri: String = "",
    private var iconColor: String = "#d59557",
    var size: Int = 0
) : BaseObservable(), Serializable {

    @Bindable
    fun getName() = name

    fun setName(value: String){
        if (name != value){
            name = value
            notifyChange()
        }
    }
    @Bindable
    fun getDescription() = description

    fun setDescription(value: String){
        if (description != value){
            description = value
            notifyChange()
        }
    }
    @Bindable
    fun getIconColor() = iconColor

    fun setIconColor(value: String){
        if (iconColor != value){
            iconColor = value
            notifyChange()
        }
    }
    @Bindable
    fun getImageUri() = imageUri

    fun setImageUri(value: String){
        if (imageUri != value){
            imageUri = value
            notifyChange()
        }
    }
    @Bindable
    fun getSizeAsString() = size.toString()

    @Bindable
    fun isFieldsCorrect() = isGroupNameUnique && name.isNotBlank()

    @Ignore
    @get:Bindable
    var isGroupNameUnique = true
        set(value) {
            field = value
            notifyChange()
        }
}
