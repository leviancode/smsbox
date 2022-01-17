package com.brainymobile.android.smsbox.domain.entities.recipient

data class RecipientWithGroups(
    var recipient: Recipient,
    var groups: List<RecipientGroup>
)
