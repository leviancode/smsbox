package com.leviancode.android.gsmbox.domain.usecases.templates.tempates.impl

import com.leviancode.android.gsmbox.domain.entities.template.Template
import com.leviancode.android.gsmbox.domain.repositories.TemplatesRepository
import com.leviancode.android.gsmbox.domain.usecases.templates.tempates.DeleteTemplatesUseCase

class DeleteTemplatesUseCaseImpl(private val repository: TemplatesRepository) :
    DeleteTemplatesUseCase {
    override suspend fun delete(item: Template) {
        repository.delete(item)
    }
}