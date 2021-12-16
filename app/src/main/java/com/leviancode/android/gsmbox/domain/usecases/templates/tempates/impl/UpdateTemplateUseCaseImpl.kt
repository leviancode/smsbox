package com.leviancode.android.gsmbox.domain.usecases.templates.tempates.impl

import com.leviancode.android.gsmbox.domain.repositories.TemplatesRepository
import com.leviancode.android.gsmbox.domain.usecases.templates.tempates.UpdateTemplateUseCase

class UpdateTemplateUseCaseImpl(private val repository: TemplatesRepository) : UpdateTemplateUseCase {
    override suspend fun updateFavorite(templateId: Int, favorite: Boolean) {
        repository.updateFavorite(templateId, favorite)
    }
}