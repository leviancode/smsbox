package com.leviancode.android.gsmbox.domain.entities.recipient

data class Recipient(
    var id: Int = 0,
    var position: Int = -1,
    var name: String?,
    var phoneNumber: String,
    val timestamp: Long
)
