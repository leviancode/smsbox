package com.leviancode.android.gsmbox.domain.usecases.recipients.recipients

import com.leviancode.android.gsmbox.domain.entities.recipient.Recipient
import com.leviancode.android.gsmbox.domain.entities.recipient.RecipientWithGroups

interface UpdateRecipientsUseCase {
    suspend fun update(items: List<Recipient>)
    suspend fun update(item: RecipientWithGroups): Int
}