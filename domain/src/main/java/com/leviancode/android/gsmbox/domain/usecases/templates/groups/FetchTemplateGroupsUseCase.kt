package com.leviancode.android.gsmbox.domain.usecases.templates.groups

import com.leviancode.android.gsmbox.domain.entities.template.TemplateGroup
import kotlinx.coroutines.flow.Flow

interface FetchTemplateGroupsUseCase {
    fun getGroupsObservable(): Flow<List<TemplateGroup>>
    suspend fun getById(id: Int): TemplateGroup?
    suspend fun getByName(name: String): TemplateGroup?
}