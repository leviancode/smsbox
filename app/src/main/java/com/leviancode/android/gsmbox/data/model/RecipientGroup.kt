package com.leviancode.android.gsmbox.data.model

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "recipient_groups")
data class RecipientGroup(
    @PrimaryKey
    @ColumnInfo(name = "group_id")
    var groupId: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "size") var size: Int = 0,
    @ColumnInfo(name = "icon_color") var iconColor: Int = Color.parseColor("#78909C"),
    @Ignore var recipients: MutableList<Recipient> = mutableListOf()
) : Serializable
