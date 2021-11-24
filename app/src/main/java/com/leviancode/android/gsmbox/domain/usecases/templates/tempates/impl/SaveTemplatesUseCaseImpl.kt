package com.leviancode.android.gsmbox.domain.usecases.templates.tempates.impl

import com.leviancode.android.gsmbox.domain.entities.template.Template
import com.leviancode.android.gsmbox.domain.repositories.TemplatesRepository
import com.leviancode.android.gsmbox.domain.usecases.templates.tempates.SaveTemplatesUseCase

class SaveTemplatesUseCaseImpl(private val repository: TemplatesRepository) :
    SaveTemplatesUseCase {
    override suspend fun save(item: Template) {
        repository.save(item)
    }

    override suspend fun save(items: List<Template>) {
        repository.save(items)
    }
}