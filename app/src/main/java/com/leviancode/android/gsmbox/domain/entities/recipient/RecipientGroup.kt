package com.leviancode.android.gsmbox.domain.entities.recipient

data class RecipientGroup(
    var id: Int = 0,
    var position: Int = 0,
    var name: String? = null,
    var iconColor: String = "",
    var recipients: List<Recipient> = listOf(),
    val timestamp: Long = System.currentTimeMillis()
)