package com.leviancode.android.gsmbox.domain.usecases.recipients.recipients

import com.leviancode.android.gsmbox.domain.entities.recipient.Recipient
import com.leviancode.android.gsmbox.domain.entities.recipient.RecipientWithGroups

interface SaveRecipientsUseCase {
    suspend fun save(item: Recipient): Int

    suspend fun save(item: RecipientWithGroups): Int
}