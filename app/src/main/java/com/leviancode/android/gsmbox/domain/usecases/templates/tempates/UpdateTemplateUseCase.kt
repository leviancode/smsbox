package com.leviancode.android.gsmbox.domain.usecases.templates.tempates

interface UpdateTemplateUseCase {
    suspend fun updateFavorite(templateId: Int, favorite: Boolean)
}