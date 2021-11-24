package com.leviancode.android.gsmbox.domain.entities.recipient

data class RecipientWithGroups(
    val recipient: Recipient,
    val groups: List<RecipientGroup>
)
