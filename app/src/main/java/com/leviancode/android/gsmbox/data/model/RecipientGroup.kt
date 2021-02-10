package com.leviancode.android.gsmbox.data.model

import java.util.*

data class RecipientGroup(
    var id: String = UUID.randomUUID().toString(),
    var name: String,
    var recipients: MutableList<Recipient> = mutableListOf()
)
