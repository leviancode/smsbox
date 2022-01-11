package com.leviancode.android.gsmbox.domain.usecases.placeholders.impl

import com.leviancode.android.gsmbox.domain.entities.placeholder.Placeholder
import com.leviancode.android.gsmbox.domain.repositories.PlaceholdersRepository
import com.leviancode.android.gsmbox.domain.usecases.placeholders.DeletePlaceholdersUseCase

class DeletePlaceholdersUseCaseImpl(private val repository: PlaceholdersRepository) :
    DeletePlaceholdersUseCase {
    override suspend fun delete(item: Placeholder) {
        repository.delete(item)
    }

    override suspend fun delete(id: Int) {
        repository.deleteById(id)
    }
}