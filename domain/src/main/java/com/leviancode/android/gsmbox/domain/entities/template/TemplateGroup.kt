package com.leviancode.android.gsmbox.domain.entities.template

data class TemplateGroup(
    val id: Int,
    var position: Int,
    val name: String,
    val description: String,
    val iconColor: String,
    val size: Int,
    val timestamp: Long
)