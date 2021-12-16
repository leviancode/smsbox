package com.leviancode.android.gsmbox.domain.usecases.recipients.groups.impl

import com.leviancode.android.gsmbox.domain.entities.recipient.RecipientGroup
import com.leviancode.android.gsmbox.domain.repositories.RecipientGroupsRepository
import com.leviancode.android.gsmbox.domain.usecases.recipients.groups.DeleteRecipientGroupsUseCase

class DeleteRecipientGroupsUseCaseImpl(private val repository: RecipientGroupsRepository) :
    DeleteRecipientGroupsUseCase {
    override suspend fun delete(item: RecipientGroup) {
        repository.delete(item)
    }

    override suspend fun deleteById(id: Int) {
        repository.delete(id)
    }

    override suspend fun clearGroup(groupId: Int) {
        repository.deleteRecipientsFromGroup(groupId)
    }

    override suspend fun unbind(groupId: Int, recipientId: Int) {
        repository.unbind(groupId, recipientId)
    }
}