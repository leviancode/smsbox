package com.leviancode.android.gsmbox.data.model

import java.util.*

data class Recipient(
    var id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var phoneNumber: String = ""
)
