package com.leviancode.android.gsmbox.model

import java.util.*

data class Device(
    var id: String = UUID.randomUUID().toString(),
    var name: String,
    var phoneNumber: String,
    var groupId: String
)
