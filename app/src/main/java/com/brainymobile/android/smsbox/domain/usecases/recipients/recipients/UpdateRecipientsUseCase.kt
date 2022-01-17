package com.brainymobile.android.smsbox.domain.usecases.recipients.recipients

import com.brainymobile.android.smsbox.domain.entities.recipient.Recipient
import com.brainymobile.android.smsbox.domain.entities.recipient.RecipientWithGroups

interface UpdateRecipientsUseCase {
    suspend fun update(items: List<Recipient>)
    suspend fun update(item: RecipientWithGroups): Int
}