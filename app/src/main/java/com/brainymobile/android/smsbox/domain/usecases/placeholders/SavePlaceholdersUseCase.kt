package com.brainymobile.android.smsbox.domain.usecases.placeholders

import com.brainymobile.android.smsbox.domain.entities.placeholder.Placeholder

interface SavePlaceholdersUseCase {
    suspend fun save(item: Placeholder)
}