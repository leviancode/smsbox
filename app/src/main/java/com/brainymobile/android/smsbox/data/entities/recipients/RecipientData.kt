package com.brainymobile.android.smsbox.data.entities.recipients

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipients")
data class RecipientData(
    @PrimaryKey(autoGenerate = true)
    var recipientId: Int = 0,
    var position: Int = -1,
    var name: String? = null,
    var phoneNumber: String,
    val timestamp: Long = System.currentTimeMillis()
)
