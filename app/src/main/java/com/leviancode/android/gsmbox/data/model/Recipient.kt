package com.leviancode.android.gsmbox.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "recipients")
data class Recipient(
    @PrimaryKey
    var recipientId: String = UUID.randomUUID().toString(),
    var recipientName: String = "",
    var phoneNumber: String = "",
    var groupName: String? = null
) : Serializable
