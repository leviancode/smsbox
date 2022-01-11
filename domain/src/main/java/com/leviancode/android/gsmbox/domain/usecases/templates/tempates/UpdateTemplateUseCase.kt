package com.leviancode.android.gsmbox.domain.usecases.templates.tempates

import com.leviancode.android.gsmbox.domain.entities.template.Template

interface UpdateTemplateUseCase {
    suspend fun updateFavorite(templateId: Int, favorite: Boolean)
    suspend fun update(items: List<Template>)
    suspend fun update(item: Template)
}