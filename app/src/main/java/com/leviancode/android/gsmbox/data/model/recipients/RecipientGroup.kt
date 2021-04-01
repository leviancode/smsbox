package com.leviancode.android.gsmbox.data.model.recipients

import android.graphics.Color
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.leviancode.android.gsmbox.BR
import com.leviancode.android.gsmbox.utils.isNotEmpty
import java.io.Serializable
import java.util.*

@Entity(tableName = "recipient_groups")
@kotlinx.serialization.Serializable
data class RecipientGroup(
    @PrimaryKey
    var recipientGroupId: String = UUID.randomUUID().toString(),
    private var recipientGroupName: String = "",
    private var recipientGroupIconColor: Int = Color.parseColor("#78909C"),
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
    var isRecipientGroupNameUnique = true
        set(value) {
            field = value
            notifyChange()
        }

    @Bindable
    fun getRecipientGroupName() = recipientGroupName
    fun setRecipientGroupName(value: String){
        if (recipientGroupName != value){
            recipientGroupName = value
            notifyChange()
        }
    }

    @Bindable
    fun getRecipientGroupIconColor() = recipientGroupIconColor
    fun setRecipientGroupIconColor(value: Int){
        if (recipientGroupIconColor != value){
            recipientGroupIconColor = value
            notifyChange()
        }
    }

    @Bindable
    fun isFieldsFilled() = isNotEmpty(getRecipientGroupName()) && isRecipientGroupNameUnique

}
