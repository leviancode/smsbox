package com.leviancode.android.gsmbox.data.model.recipients

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.utils.isNotNullOrEmpty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable

@Entity(tableName = "recipient_groups")
@kotlinx.serialization.Serializable
data class RecipientGroup(
    @PrimaryKey(autoGenerate = true)
    var recipientGroupId: Long = 0L,
    private var name: String? = null,
    private var iconColor: String = "#d59557",
) : BaseObservable(), Serializable {

    @Ignore
    @get:Bindable
    var selected = false
        set(value) {
            field = value
            notifyChange()
        }

    @Ignore
    @get:Bindable
    var isNameUnique = true
        set(value) {
            field = value
            notifyChange()
        }

    fun isNameNotNullOrEmpty() = isNotNullOrEmpty(name)

    @Bindable
    fun getName() = name
    fun setName(value: String?){
        if (name != value){
            name = value
            notifyChange()
            checkNameIsUnique(value)
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
    fun isFieldsCorrect() = isNotNullOrEmpty(name) && isNameUnique

    private fun checkNameIsUnique(value: String?){
        if (value == null) {
            isNameUnique = true
            return
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                val found = RecipientsRepository.getGroupByName(value)
                isNameUnique = found == null || found.recipientGroupId == recipientGroupId
            }
        }
    }

}
