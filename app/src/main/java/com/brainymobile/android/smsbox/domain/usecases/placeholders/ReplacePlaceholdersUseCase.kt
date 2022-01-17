package com.brainymobile.android.smsbox.domain.usecases.placeholders

interface ReplacePlaceholdersUseCase {
    suspend fun replace(str: String): String
}