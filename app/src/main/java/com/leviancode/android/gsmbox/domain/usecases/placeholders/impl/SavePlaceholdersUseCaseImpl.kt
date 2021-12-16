package com.leviancode.android.gsmbox.domain.usecases.placeholders.impl

import com.leviancode.android.gsmbox.domain.entities.placeholder.Placeholder
import com.leviancode.android.gsmbox.domain.repositories.PlaceholdersRepository
import com.leviancode.android.gsmbox.domain.usecases.placeholders.SavePlaceholdersUseCase

class SavePlaceholdersUseCaseImpl(private val repository: PlaceholdersRepository) :
    SavePlaceholdersUseCase {
    override suspend fun save(item: Placeholder) {
        repository.save(item)
    }
}