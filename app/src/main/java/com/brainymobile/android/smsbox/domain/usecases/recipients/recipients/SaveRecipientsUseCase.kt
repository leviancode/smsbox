package com.brainymobile.android.smsbox.domain.usecases.recipients.recipients

import com.brainymobile.android.smsbox.domain.entities.recipient.Recipient
import com.brainymobile.android.smsbox.domain.entities.recipient.RecipientWithGroups

interface SaveRecipientsUseCase {
    suspend fun save(item: Recipient): Int

    suspend fun save(item: RecipientWithGroups): Int
}