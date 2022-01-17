package com.brainymobile.android.smsbox.domain.usecases.templates.tempates

import com.brainymobile.android.smsbox.domain.entities.template.Template

interface UpdateTemplateUseCase {
    suspend fun updateFavorite(templateId: Int, favorite: Boolean)
    suspend fun update(items: List<Template>)
    suspend fun update(item: Template)
}