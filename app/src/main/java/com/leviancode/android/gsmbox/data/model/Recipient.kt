package com.leviancode.android.gsmbox.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "recipients")
data class Recipient(
    @PrimaryKey
    @ColumnInfo(name = "recipient_id")
    var recipientId: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "phone_number") var phoneNumber: String = ""
)
