package com.brainymobile.android.smsbox.domain.usecases.placeholders.impl

import com.brainymobile.android.smsbox.domain.entities.placeholder.Placeholder
import com.brainymobile.android.smsbox.domain.repositories.PlaceholdersRepository
import com.brainymobile.android.smsbox.domain.usecases.placeholders.SavePlaceholdersUseCase
import javax.inject.Inject

class SavePlaceholdersUseCaseImpl @Inject constructor(private val repository: PlaceholdersRepository) :
    SavePlaceholdersUseCase {
    override suspend fun save(item: Placeholder) {
        repository.save(item)
    }
}