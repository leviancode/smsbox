package com.leviancode.android.gsmbox.data.model

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "templates")
data class Template(
    @PrimaryKey
    @ColumnInfo(name = "template_id")
    var templateId: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "group_id") var groupId: String = "",
    var name: String = "",
    var message: String = "",
    @ColumnInfo(name = "icon_color") var iconColor: Int = Color.parseColor("#66BB6A"),
    var favorite: Boolean = false,
    var recipients: MutableList<Recipient> = mutableListOf(),
    @ColumnInfo(name = "recipient_group_name") var recipientGroupName: String = ""
) : Serializable
