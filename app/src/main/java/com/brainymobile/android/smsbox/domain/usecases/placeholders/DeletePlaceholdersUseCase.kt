package com.brainymobile.android.smsbox.domain.usecases.placeholders

import com.brainymobile.android.smsbox.domain.entities.placeholder.Placeholder

interface DeletePlaceholdersUseCase {
    suspend fun delete(item: Placeholder)
    suspend fun delete(id: Int)
}