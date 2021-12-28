package com.leviancode.android.gsmbox.domain.usecases.templates.tempates.impl

import com.leviancode.android.gsmbox.domain.entities.template.Template
import com.leviancode.android.gsmbox.domain.repositories.TemplatesRepository
import com.leviancode.android.gsmbox.domain.usecases.templates.tempates.UpdateTemplateUseCase

class UpdateTemplateUseCaseImpl(private val repository: TemplatesRepository) : UpdateTemplateUseCase {
    override suspend fun updateFavorite(templateId: Int, favorite: Boolean) {
        repository.updateFavorite(templateId, favorite)
    }

    override suspend fun update(items: List<Template>) {
        repository.update(items)
    }

    override suspend fun update(item: Template) {
        repository.update(item)
    }
}