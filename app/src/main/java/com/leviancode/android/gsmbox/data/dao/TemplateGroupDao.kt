package com.leviancode.android.gsmbox.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.leviancode.android.gsmbox.data.model.templates.GroupWithTemplates
import com.leviancode.android.gsmbox.data.model.templates.TemplateGroup

@Dao
interface TemplateGroupDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: TemplateGroup): Long

    @Update suspend fun update(item: TemplateGroup)
    @Delete suspend fun delete(vararg item: TemplateGroup)

    @Query("SELECT * from template_groups WHERE templateGroupId = :id")
    suspend fun get(id: Long): TemplateGroup?

    @Query("SELECT * from template_groups WHERE name = :name")
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
    fun getGroupWithTemplates(groupId: Long): LiveData<GroupWithTemplates>

    @Query("DELETE FROM template_groups")
    suspend fun deleteAll()
}