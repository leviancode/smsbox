package com.brainymobile.android.smsbox.domain.usecases.recipients.groups

import com.brainymobile.android.smsbox.domain.entities.recipient.RecipientGroup

interface SaveRecipientGroupsUseCase {
    suspend fun save(item: RecipientGroup): Int
    suspend fun save(items: List<RecipientGroup>): IntArray
}