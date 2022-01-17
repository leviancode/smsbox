package com.brainymobile.android.smsbox.domain.usecases.templates.groups

import com.brainymobile.android.smsbox.domain.entities.template.TemplateGroup

interface DeleteTemplateGroupUseCase {
    suspend fun delete(item: TemplateGroup)
}