package com.leviancode.android.gsmbox.data.entities.recipients

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.leviancode.android.gsmbox.utils.DEFAULT_GROUP_COLOR
import com.leviancode.android.gsmbox.utils.getFormatDate

@Entity(tableName = "recipient_groups")
data class RecipientGroupData(
    @PrimaryKey(autoGenerate = true)
    var recipientGroupId: Int = 0,
    var position: Int = 0,
    var name: String? = null,
    var iconColor: String = DEFAULT_GROUP_COLOR,
    val timestamp: Long = System.currentTimeMillis()
)