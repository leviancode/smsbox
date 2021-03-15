package com.leviancode.android.gsmbox.data.model.templates

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "template_groups")
@kotlinx.serialization.Serializable
data class  TemplateGroup (
    @PrimaryKey
    @ColumnInfo(name = "group_id")
    var groupId: String = UUID.randomUUID().toString(),
    var name: String = "",
    var description: String = "",
    @ColumnInfo(name = "image_uri") var imageUri: String = "",
    @ColumnInfo(name = "icon_color") var iconColor: Int = Color.parseColor("#78909C"),
    var size: Int = 0
) : Serializable
