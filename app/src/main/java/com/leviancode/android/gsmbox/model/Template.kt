package com.leviancode.android.gsmbox.model

import android.graphics.Color
import java.util.*

data class Template(
    var id: String = UUID.randomUUID().toString(),
    var name: String,
    var command: String,
    var discription: String = "",
    var groupId: String,
    val devices: MutableList<DeviceGroup> = mutableListOf(),
    val isFavorite: Boolean = false,
    var color: Int? = Color.parseColor("#FF018786")
)
