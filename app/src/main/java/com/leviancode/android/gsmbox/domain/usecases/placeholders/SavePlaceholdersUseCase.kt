package com.leviancode.android.gsmbox.domain.usecases.placeholders

import com.leviancode.android.gsmbox.domain.entities.placeholder.Placeholder

interface SavePlaceholdersUseCase {
    suspend fun save(item: Placeholder)
}