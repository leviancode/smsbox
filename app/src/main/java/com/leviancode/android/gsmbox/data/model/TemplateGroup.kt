package com.leviancode.android.gsmbox.data.model

import java.util.*

data class  TemplateGroup (
    var id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var description: String = "",
    var imageUri: String? = null,
    var iconColor: Int? = null,
    var templates: MutableList<Template> = mutableListOf()
)
