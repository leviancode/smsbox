package com.leviancode.android.gsmbox.core.data.model.recipients

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.leviancode.android.gsmbox.core.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.core.utils.getFormatDate
import com.leviancode.android.gsmbox.core.utils.isNotNullOrEmpty
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.io.Serializable

@Entity(tableName = "recipients")
@kotlinx.serialization.Serializable
data class Recipient(
    @PrimaryKey(autoGenerate = true)
    var recipientId: Int = 0,
    private var name: String? = null,
    private var phoneNumber: String = "",
    val date: String = getFormatDate()
) : BaseObservable(), Serializable {

    @Ignore
    @get:Bindable
    var isPhoneNumberUnique: Boolean = false
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
            checkPhoneNumberIsUnique(value)
        }
    }

    @Bindable
    fun isFieldsCorrect() = isNotNullOrEmpty(name, phoneNumber) && isNameUnique

    private fun checkNameIsUnique(value: String?){
        if (value == null) {
            isNameUnique = true
            return
        } else {
            CoroutineScope(IO).launch {
                val found = RecipientsRepository.getRecipientByName(value)
                withContext(Main){
                    isNameUnique = found == null || found.recipientId == recipientId
                }
            }
        }
    }

    private fun checkPhoneNumberIsUnique(value: String?){
        if (value == null) {
            isPhoneNumberUnique = true
            return
        } else {
            CoroutineScope(Main).launch {
                val found = async {
                    RecipientsRepository.getRecipientByPhoneNumber(value)
                }
                isPhoneNumberUnique = found.await() == null
            }
        }
    }
}
