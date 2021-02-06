package com.leviancode.android.gsmbox.data.model

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.leviancode.android.gsmbox.R
import java.util.*

@Entity(tableName = "templates")
data class Template(
    @PrimaryKey
    @ColumnInfo(name = "template_id")
    var templateId: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "group_id") var groupId: String = "",
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "message") var message: String = "",
    @ColumnInfo(name = "icon_color") var iconColor: Int = Color.parseColor("#66BB6A"),
    @ColumnInfo(name = "favorite") var favorite: Boolean = false,
    var recipients: MutableList<String> = mutableListOf()
)
