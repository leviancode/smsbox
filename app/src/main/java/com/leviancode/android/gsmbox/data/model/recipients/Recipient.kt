package com.leviancode.android.gsmbox.data.model.recipients

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.utils.isNotNullOrEmpty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable

@Entity(tableName = "recipients")
@kotlinx.serialization.Serializable
data class Recipient(
    @PrimaryKey(autoGenerate = true)
    var recipientId: Long = 0L,
    private var name: String? = null,
    private var phoneNumber: String = ""
) : BaseObservable(), Serializable {

    @Ignore
    @get:Bindable
    var isNameUnique = true
        set(value) {
            field = value
            notifyChange()
        }

    @Ignore
    @get:Bindable
    var selected = false
        set(value) {
            field = value
            notifyChange()
        }

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
    fun getPhoneNumber() = phoneNumber
    fun setPhoneNumber(value: String){
        if (phoneNumber != value){
            phoneNumber = value
            notifyChange()
        }
    }

    @Bindable
    fun isFieldsCorrect() = isNotNullOrEmpty(name, phoneNumber) && isNameUnique

    private fun checkNameIsUnique(value: String?){
        if (value == null) {
            isNameUnique = true
            return
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                val found = RecipientsRepository.getRecipientByName(value)
                isNameUnique = found == null || found.recipientId == recipientId
            }
        }
    }
}
