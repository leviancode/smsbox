package com.leviancode.android.gsmbox.data.model

import android.graphics.Color
import java.util.*

data class Template(
    var id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var message: String = "",
    var description: String = "",
    var recipients: MutableList<Recipient> = mutableListOf(),
    var iconColor: Int? = null,
    var favorite: Boolean = false
)
