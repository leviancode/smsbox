package com.leviancode.android.gsmbox.data.repositories

import com.leviancode.android.gsmbox.data.database.dao.templates.TemplateGroupDao
import com.leviancode.android.gsmbox.data.entities.templates.group.toDataGroup
import com.leviancode.android.gsmbox.data.entities.templates.group.toDomainGroup
import com.leviancode.android.gsmbox.data.entities.templates.group.toDomainGroups
import com.leviancode.android.gsmbox.domain.entities.template.TemplateGroup
import com.leviancode.android.gsmbox.domain.repositories.TemplateGroupsRepository
import com.leviancode.android.gsmbox.domain.repositories.TemplatesRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class TemplateGroupsRepositoryImpl(
    private val groupsDao: TemplateGroupDao,
    private val templatesRepository: TemplatesRepository
): TemplateGroupsRepository {

    override fun getGroupsObservable() = groupsDao.getGroupsWithTemplatesObservable().map { it.toDomainGroups() }

    override fun getGroupNames() = groupsDao.getGroupNames()

    override suspend fun getById(id: Int) = groupsDao.getGroupWithTemplates(id)?.toDomainGroup()

    override suspend fun getByName(name: String) = groupsDao.getGroupWithTemplatesByName(name)?.toDomainGroup()

    override suspend fun save(item: TemplateGroup) = withContext(IO) {
        groupsDao.upsert(item.toDataGroup())
    }

    override suspend fun save(items: List<TemplateGroup>) = withContext(IO) {
        items.map { save(it) }.toIntArray()
    }

    override suspend fun delete(item: TemplateGroup) = withContext(IO) {
        templatesRepository.deleteByGroupId(item.id)
        groupsDao.delete(item.toDataGroup())
    }

    override suspend fun replaceGroupsPosition(first: Int, second: Int) = withContext(IO) {
        val tempId = Int.MAX_VALUE
    /*    groupsDao.updatePosition(firstId, tempId)
        groupsDao.updatePosition(secondId, firstId)
        groupsDao.updatePosition(tempId, secondId)*/
    }
}