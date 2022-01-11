package com.leviancode.android.gsmbox.domain.entities.template

import com.leviancode.android.gsmbox.domain.entities.recipient.RecipientGroup

data class Template(
    val id: Int = 0,
    var position: Int,
    val groupId: Int,
    val name: String,
    val message: String,
    val iconColor: String,
    val favorite: Boolean,
    val recipientGroup: RecipientGroup,
    val timestamp: Long
)
