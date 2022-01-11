package com.leviancode.android.gsmbox.domain.usecases.templates.groups

import com.leviancode.android.gsmbox.domain.entities.template.TemplateGroup

interface DeleteTemplateGroupUseCase {
    suspend fun delete(item: TemplateGroup)
}