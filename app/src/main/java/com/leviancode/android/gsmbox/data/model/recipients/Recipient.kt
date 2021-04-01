package com.leviancode.android.gsmbox.data.model.recipients

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.leviancode.android.gsmbox.BR
import com.leviancode.android.gsmbox.utils.isNotEmpty
import java.io.Serializable
import java.util.*

@Entity(tableName = "recipients")
@kotlinx.serialization.Serializable
data class Recipient(
    @PrimaryKey
    var recipientId: String = UUID.randomUUID().toString(),
    private var recipientName: String = "",
    private var phoneNumber: String = ""
) : BaseObservable(), Serializable {

    @Ignore
    @get:Bindable
    var isRecipientNameUnique = true
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
    fun getRecipientName() = recipientName
    fun setRecipientName(value: String){
        if (recipientName != value){
            recipientName = value
            notifyChange()
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
    fun isFieldsFilled() = isNotEmpty(recipientName, phoneNumber) && isRecipientNameUnique
}
