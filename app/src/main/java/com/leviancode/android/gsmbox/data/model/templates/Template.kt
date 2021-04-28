package com.leviancode.android.gsmbox.data.model.templates

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.*
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.utils.DEFAULT_TEMPLATE_COLOR
import com.leviancode.android.gsmbox.utils.getFormatDate
import com.leviancode.android.gsmbox.utils.isNotNullOrEmpty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable


@Entity(tableName = "templates",
    foreignKeys = [ForeignKey(
        entity = RecipientGroup::class,
        parentColumns = arrayOf("recipientGroupId"),
        childColumns = arrayOf("recipientGroupId"),
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE,
        deferred = true
    ),
        ForeignKey(
            entity = TemplateGroup::class,
            parentColumns = arrayOf("templateGroupId"),
            childColumns = arrayOf("templateGroupId"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            deferred = true
        )]
)
@kotlinx.serialization.Serializable
data class Template(
    @PrimaryKey(autoGenerate = true)
    var templateId: Int = 0,
    var templateGroupId: Int = 0,
    var recipientGroupId: Int? = null,
    private var name: String = "",
    private var message: String = "",
    private var iconColor: String = DEFAULT_TEMPLATE_COLOR,
    private var favorite: Boolean = false,
    val date: String = getFormatDate()
) : BaseObservable(), Serializable {

    @Bindable
    fun getName() = name
    fun setName(value: String) {
        if (name != value) {
            name = value
            notifyChange()
        }
    }

    @Bindable
    fun getMessage() = message
    fun setMessage(value: String) {
        if (message != value) {
            message = value
            notifyChange()
            checkNameIsUnique(value)
        }
    }

    @Bindable
    fun isFavorite() = favorite
    fun setFavorite(value: Boolean) {
        if (favorite != value) {
            favorite = value
            notifyChange()
        }
    }

    @Bindable
    fun getIconColor() = iconColor
    fun setIconColor(value: String) {
        if (iconColor != value) {
            iconColor = value
            notifyChange()
        }
    }

    @Ignore
    @get:Bindable
    var isNameUnique = true
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    fun isFieldsCorrect() = isNameUnique && isNotNullOrEmpty(
        getName(),
        getMessage()
    )

    private fun checkNameIsUnique(value: String){
        CoroutineScope(Dispatchers.IO).launch {
            val found = TemplatesRepository.getTemplateByName(value)
            isNameUnique = found == null || found.templateId == templateId
        }
    }
}
