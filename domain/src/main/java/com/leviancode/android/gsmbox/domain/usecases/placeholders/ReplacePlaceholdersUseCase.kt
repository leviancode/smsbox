package com.leviancode.android.gsmbox.domain.usecases.placeholders

interface ReplacePlaceholdersUseCase {
    suspend fun replace(str: String): String
}