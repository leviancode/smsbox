package com.leviancode.android.gsmbox.domain.usecases.recipients.groups

import com.leviancode.android.gsmbox.domain.entities.recipient.RecipientGroup

interface DeleteRecipientGroupsUseCase {
    suspend fun delete(item: RecipientGroup)
    suspend fun deleteById(id: Int)
    suspend fun clearGroup(groupId: Int)
    suspend fun unbind(groupId: Int, recipientId: Int)
}