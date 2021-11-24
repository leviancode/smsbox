package com.leviancode.android.gsmbox.domain.usecases.templates.groups

import com.leviancode.android.gsmbox.domain.entities.template.TemplateGroup

interface SaveTemplateGroupUseCase {
    suspend fun save(item: TemplateGroup)
}