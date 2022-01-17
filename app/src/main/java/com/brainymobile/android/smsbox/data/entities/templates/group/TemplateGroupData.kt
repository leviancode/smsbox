package com.brainymobile.android.smsbox.data.entities.templates.group

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.brainymobile.android.smsbox.utils.DEFAULT_GROUP_COLOR

@Entity(tableName = "template_groups")
data class  TemplateGroupData (
    @PrimaryKey(autoGenerate = true)
    var templateGroupId: Int = 0,
    val position: Int,
    val name: String,
    val description: String,
    val imageUri: String = "",
    val iconColor: String = DEFAULT_GROUP_COLOR,
    val timestamp: Long = System.currentTimeMillis()
)
