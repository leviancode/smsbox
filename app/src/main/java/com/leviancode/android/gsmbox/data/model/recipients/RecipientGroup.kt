package com.leviancode.android.gsmbox.data.model.recipients

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "recipient_groups")
@kotlinx.serialization.Serializable
data class RecipientGroup(
    @PrimaryKey
    var groupId: String = UUID.randomUUID().toString(),
    var groupName: String = "",
    var groupIconColor: Int = Color.parseColor("#78909C"),
    @Ignore var size: Int = 0
) : Serializable
