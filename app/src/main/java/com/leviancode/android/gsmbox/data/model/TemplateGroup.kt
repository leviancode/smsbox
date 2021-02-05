package com.leviancode.android.gsmbox.data.model

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.leviancode.android.gsmbox.R
import java.util.*

@Entity(tableName = "template_groups")
data class  TemplateGroup (
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "image_uri") var imageUri: String? = null,
    @ColumnInfo(name = "icon_color") var iconColor: Int = Color.parseColor("#90A4AE"),
    @ColumnInfo(name = "size") var size: Int = 0
)
