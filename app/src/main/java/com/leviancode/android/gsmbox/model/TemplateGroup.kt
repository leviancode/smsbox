package com.leviancode.android.gsmbox.model

import android.graphics.Bitmap
import java.util.*
import kotlin.collections.ArrayList

data class TemplateGroup(
    var id: String = UUID.randomUUID().toString(),
    var name: String,
    var description: String = "",
    var image: Bitmap? = null,
    var imageUri: String? = null,
    var iconColor: Int? = null
) : ArrayList<Template>()
