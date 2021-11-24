package com.leviancode.android.gsmbox.data.entities.recipients

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.leviancode.android.gsmbox.utils.getFormatDate

@Entity(tableName = "recipients")
data class RecipientData(
    @PrimaryKey(autoGenerate = true)
    var recipientId: Int = 0,
    var name: String? = null,
    var phoneNumber: String,
    val date: String = getFormatDate()
)
