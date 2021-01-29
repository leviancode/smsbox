package com.leviancode.android.gsmbox.data.model

import android.graphics.Color
import java.util.*

data class Template(
    var id: String = UUID.randomUUID().toString(),
    var groupId: String = "",
    var name: String = "",
    var message: String = "",
    var iconColor: Int = Color.parseColor("#FF6200EE"),
    var favorite: Boolean = false,
    var recipients: MutableList<Recipient> = mutableListOf()
)
