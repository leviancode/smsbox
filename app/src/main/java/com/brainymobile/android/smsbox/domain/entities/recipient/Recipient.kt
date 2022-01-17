package com.brainymobile.android.smsbox.domain.entities.recipient

data class Recipient(
    var id: Int = 0,
    var position: Int = -1,
    var name: String?,
    var phoneNumber: String,
    val timestamp: Long
)
