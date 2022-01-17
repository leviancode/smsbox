package com.brainymobile.android.smsbox.domain.repositories

import com.brainymobile.android.smsbox.domain.entities.template.Template
import kotlinx.coroutines.flow.Flow

interface TemplatesRepository {

    fun getTemplatesByGroupIdObservable(groupId: Int): Flow<List<Template>>

    suspend fun getByName(name: String): Template?

    suspend fun getById(templateId: Int): Template?

    fun getFavoritesObservable(): Flow<List<Template>>

    suspend fun getFavorites(): List<Template>

    fun getFavoritesSync(): List<Template>

    fun getTemplateNamesExclusiveById(id: Int): Flow<List<String>>

    suspend fun save(item: Template): Int

    suspend fun updateFavorite(templateId: Int, favorite: Boolean): Int

    suspend fun delete(item: Template)

    suspend fun deleteByGroupId(groupId: Int)

    suspend fun update(items: List<Template>)

    suspend fun update(item: Template)

    suspend fun count(): Int
}