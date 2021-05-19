package com.leviancode.android.gsmbox.core.data.dao.templates

import androidx.lifecycle.LiveData
import androidx.room.*
import com.leviancode.android.gsmbox.core.data.model.templates.GroupWithTemplates
import com.leviancode.android.gsmbox.core.data.model.templates.TemplateGroup

@Dao
interface TemplateGroupDao {
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: TemplateGroup): Long

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<TemplateGroup>): LongArray

    @Transaction
    suspend fun upsert(item: TemplateGroup): Int {
        return if (item.templateGroupId == 0) insert(item).toInt()
        else {
            update(item)
            item.templateGroupId
        }
    }

    @Update suspend fun update(vararg item: TemplateGroup)

    @Delete suspend fun delete(vararg item: TemplateGroup)

    @Query("SELECT * from template_groups WHERE templateGroupId = :id")
    suspend fun get(id: Int): TemplateGroup?

    @Query("SELECT * from template_groups WHERE name LIKE :name")
    suspend fun getByName(name: String): TemplateGroup?

    @Query("SELECT * FROM template_groups")
    fun getAllLiveData(): LiveData<List<TemplateGroup>>

    @Query("SELECT name FROM template_groups")
    fun getGroupNames(): LiveData<List<String>>

    @Query("SELECT * FROM template_groups")
    suspend fun getAll(): List<TemplateGroup>

    @Transaction
    @Query("SELECT * FROM template_groups")
    fun getGroupsWithTemplates(): LiveData<List<GroupWithTemplates>>

    @Transaction
    @Query("SELECT * FROM template_groups WHERE templateGroupId =:groupId")
    fun getGroupWithTemplates(groupId: Int): LiveData<GroupWithTemplates>

    @Query("DELETE FROM template_groups")
    suspend fun deleteAll()

    @Query("UPDATE template_groups SET templateGroupId = :newId WHERE templateGroupId = :targetId ")
    fun updateId(targetId: Int, newId: Int)
}