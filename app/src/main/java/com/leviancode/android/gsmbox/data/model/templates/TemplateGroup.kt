package com.leviancode.android.gsmbox.data.model.templates

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.utils.DEFAULT_GROUP_COLOR
import com.leviancode.android.gsmbox.utils.getFormatDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable

@Entity(tableName = "template_groups")
@kotlinx.serialization.Serializable
data class  TemplateGroup (
    @PrimaryKey(autoGenerate = true)
    var templateGroupId: Int = 0,
    private var name: String = "",
    private var description: String = "",
    private var imageUri: String = "",
    private var iconColor: String = DEFAULT_GROUP_COLOR,
    val date: String = getFormatDate()
) : BaseObservable(), Serializable {

    @Bindable
    fun getName() = name

    fun setName(value: String){
        if (name != value){
            name = value
            notifyChange()
            checkNameIsUnique(value)
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
    fun isFieldsCorrect() = isNameUnique && name.isNotBlank()

    @Ignore
    @get:Bindable
    var isNameUnique = true
        set(value) {
            field = value
            notifyChange()
        }

    private fun checkNameIsUnique(value: String){
        CoroutineScope(Dispatchers.IO).launch {
            val found = TemplatesRepository.getGroupByName(value)
            isNameUnique = found == null || found.templateGroupId == templateGroupId
        }
    }

}
