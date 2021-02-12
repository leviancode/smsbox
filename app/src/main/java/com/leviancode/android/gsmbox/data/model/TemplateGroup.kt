package com.leviancode.android.gsmbox.data.model

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "template_groups")
data class  TemplateGroup (
    @PrimaryKey
    @ColumnInfo(name = "group_id")
    var groupId: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "image_uri") var imageUri: String = "",
    @ColumnInfo(name = "icon_color") var iconColor: Int = Color.parseColor("#78909C"),
    @ColumnInfo(name = "size") var size: Int = 0
) : Serializable
