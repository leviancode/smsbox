package com.brainymobile.android.smsbox.domain.usecases.recipients.recipients

import com.brainymobile.android.smsbox.domain.entities.recipient.Recipient
import com.brainymobile.android.smsbox.domain.entities.recipient.RecipientWithGroups

interface DeleteRecipientsUseCase {
    suspend fun delete(item: Recipient)
    suspend fun delete(item: RecipientWithGroups)
}