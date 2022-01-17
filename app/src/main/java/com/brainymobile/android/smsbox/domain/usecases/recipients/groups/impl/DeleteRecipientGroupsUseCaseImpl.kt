package com.brainymobile.android.smsbox.domain.usecases.recipients.groups.impl

import com.brainymobile.android.smsbox.domain.entities.recipient.RecipientGroup
import com.brainymobile.android.smsbox.domain.repositories.RecipientGroupsRepository
import com.brainymobile.android.smsbox.domain.usecases.recipients.groups.DeleteRecipientGroupsUseCase

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