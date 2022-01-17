package com.brainymobile.android.smsbox.domain.entities.recipient

data class RecipientGroup(
    var id: Int = 0,
    var position: Int = -1,
    var name: String? = null,
    var iconColor: String = "",
    var recipients: List<Recipient> = listOf(),
    val timestamp: Long = System.currentTimeMillis()
)