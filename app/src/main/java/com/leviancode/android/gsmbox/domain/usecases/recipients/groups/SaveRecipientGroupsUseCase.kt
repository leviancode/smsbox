package com.leviancode.android.gsmbox.domain.usecases.recipients.groups

import com.leviancode.android.gsmbox.domain.entities.recipient.RecipientGroup

interface SaveRecipientGroupsUseCase {
    suspend fun save(item: RecipientGroup): Int
    suspend fun save(items: List<RecipientGroup>): IntArray
}