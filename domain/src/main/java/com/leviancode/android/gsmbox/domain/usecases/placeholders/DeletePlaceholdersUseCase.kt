package com.leviancode.android.gsmbox.domain.usecases.placeholders

import com.leviancode.android.gsmbox.domain.entities.placeholder.Placeholder

interface DeletePlaceholdersUseCase {
    suspend fun delete(item: Placeholder)
    suspend fun delete(id: Int)
}