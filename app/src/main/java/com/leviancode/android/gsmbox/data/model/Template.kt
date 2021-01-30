package com.leviancode.android.gsmbox.data.model

import android.graphics.Color
import com.leviancode.android.gsmbox.R
import java.util.*

data class Template(
    var id: String = UUID.randomUUID().toString(),
    var groupId: String = "",
    var name: String = "",
    var message: String = "",
    var iconColor: Int = Color.parseColor("#66BB6A"),
    var favorite: Boolean = false,
    var recipients: MutableList<Recipient> = mutableListOf()
)
