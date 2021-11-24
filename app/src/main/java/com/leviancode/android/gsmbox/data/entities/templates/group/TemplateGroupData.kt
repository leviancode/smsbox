package com.leviancode.android.gsmbox.data.entities.templates.group

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.leviancode.android.gsmbox.utils.DEFAULT_GROUP_COLOR
import com.leviancode.android.gsmbox.utils.getFormatDate

@Entity(tableName = "template_groups")
data class  TemplateGroupData (
    @PrimaryKey(autoGenerate = true)
    var templateGroupId: Int = 0,
    val name: String,
    val description: String,
    val imageUri: String = "",
    val iconColor: String = DEFAULT_GROUP_COLOR,
    val date: String = getFormatDate()
)
