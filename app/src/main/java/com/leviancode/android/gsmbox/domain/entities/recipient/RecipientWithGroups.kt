package com.leviancode.android.gsmbox.domain.entities.recipient

data class RecipientWithGroups(
    var recipient: Recipient,
    var groups: List<RecipientGroup>
)
