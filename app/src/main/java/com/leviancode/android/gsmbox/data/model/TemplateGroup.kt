package com.leviancode.android.gsmbox.data.model

import android.graphics.Color
import com.leviancode.android.gsmbox.R
import java.util.*

data class  TemplateGroup (
    var id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var description: String = "",
    var imageUri: String? = null,
    var iconColor: Int = Color.parseColor("#90A4AE"),
    var size: Int = 0
)
