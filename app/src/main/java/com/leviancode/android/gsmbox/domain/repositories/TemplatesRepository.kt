package com.leviancode.android.gsmbox.domain.repositories

import com.leviancode.android.gsmbox.domain.entities.template.Template
import kotlinx.coroutines.flow.Flow

interface TemplatesRepository {

    fun getGroupedObservable(groupId: Int): Flow<List<Template>>

    suspend fun getByName(name: String): Template?

    suspend fun getById(templateId: Int): Template?

    fun getFavoritesObservable(): Flow<List<Template>>

    suspend fun getFavorites(): List<Template>

    fun getFavoritesSync(): List<Template>

    fun getTemplateNamesExclusiveById(id: Int): Flow<List<String>>

    suspend fun save(items: List<Template>): IntArray

    suspend fun save(item: Template): Int

    suspend fun delete(item: Template)

    suspend fun deleteByGroupId(groupId: Int)
}