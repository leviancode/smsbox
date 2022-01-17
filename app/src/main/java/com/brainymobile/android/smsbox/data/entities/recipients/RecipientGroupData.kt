package com.brainymobile.android.smsbox.data.entities.recipients

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.brainymobile.android.smsbox.utils.DEFAULT_GROUP_COLOR

@Entity(tableName = "recipient_groups")
data class RecipientGroupData(
    @PrimaryKey(autoGenerate = true)
    var recipientGroupId: Int = 0,
    var position: Int = -1,
    var name: String? = null,
    var iconColor: String = DEFAULT_GROUP_COLOR,
    val timestamp: Long = System.currentTimeMillis()
)