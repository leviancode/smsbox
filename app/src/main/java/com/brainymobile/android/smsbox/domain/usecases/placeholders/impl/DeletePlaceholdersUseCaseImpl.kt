package com.brainymobile.android.smsbox.domain.usecases.placeholders.impl

import com.brainymobile.android.smsbox.domain.entities.placeholder.Placeholder
import com.brainymobile.android.smsbox.domain.repositories.PlaceholdersRepository
import com.brainymobile.android.smsbox.domain.usecases.placeholders.DeletePlaceholdersUseCase

class DeletePlaceholdersUseCaseImpl(private val repository: PlaceholdersRepository) :
    DeletePlaceholdersUseCase {
    override suspend fun delete(item: Placeholder) {
        repository.delete(item)
    }

    override suspend fun delete(id: Int) {
        repository.deleteById(id)
    }
}