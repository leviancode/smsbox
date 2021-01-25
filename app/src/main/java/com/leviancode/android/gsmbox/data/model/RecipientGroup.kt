package com.leviancode.android.gsmbox.data.model

import java.util.*
import kotlin.collections.ArrayList

data class RecipientGroup(
    var id: String = UUID.randomUUID().toString(),
    var name: String,
    var recipients: MutableList<Recipient> = mutableListOf()
)
