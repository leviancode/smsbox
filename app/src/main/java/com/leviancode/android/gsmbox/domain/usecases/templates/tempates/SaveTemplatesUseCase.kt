package com.leviancode.android.gsmbox.domain.usecases.templates.tempates

import com.leviancode.android.gsmbox.domain.entities.template.Template

interface SaveTemplatesUseCase {
    suspend fun save(item: Template)
    suspend fun save(items: List<Template>)
}