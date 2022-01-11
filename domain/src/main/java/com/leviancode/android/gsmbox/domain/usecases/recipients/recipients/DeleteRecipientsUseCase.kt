package com.leviancode.android.gsmbox.domain.usecases.recipients.recipients

import com.leviancode.android.gsmbox.domain.entities.recipient.Recipient
import com.leviancode.android.gsmbox.domain.entities.recipient.RecipientWithGroups

interface DeleteRecipientsUseCase {
    suspend fun delete(item: Recipient)
    suspend fun delete(item: RecipientWithGroups)
}