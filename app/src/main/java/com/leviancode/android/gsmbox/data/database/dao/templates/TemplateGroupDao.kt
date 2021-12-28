package com.leviancode.android.gsmbox.data.database.dao.templates

import androidx.room.*
import com.leviancode.android.gsmbox.data.entities.templates.group.GroupWithTemplates
import com.leviancode.android.gsmbox.data.entities.templates.group.TemplateGroupData
import kotlinx.coroutines.flow.Flow

@Dao
interface TemplateGroupDao {
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: TemplateGroupData): Long

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<TemplateGroupData>): LongArray

    @Transaction
    suspend fun upsert(item: TemplateGroupData): Int {
        return if (item.templateGroupId == 0) insert(item).toInt()
        else {
            update(item)
            item.templateGroupId
        }
    }

    @Update suspend fun update(vararg item: TemplateGroupData)

    @Delete suspend fun delete(vararg item: TemplateGroupData)

    @Query("SELECT * from template_groups WHERE templateGroupId = :id")
    suspend fun get(id: Int): TemplateGroupData?

    @Query("SELECT * from template_groups WHERE name LIKE :name")
    suspend fun getByName(name: String): TemplateGroupData?

    @Query("SELECT * FROM template_groups")
    fun getAllObservable(): Flow<List<TemplateGroupData>>

    @Query("SELECT name FROM template_groups")
    fun getGroupNames(): Flow<List<String>>

    @Query("SELECT * FROM template_groups")
    suspend fun getAll(): List<TemplateGroupData>

    @Transaction
    @Query("SELECT * FROM template_groups")
    fun getGroupsWithTemplatesObservable(): Flow<List<GroupWithTemplates>>

    @Transaction
    @Query("SELECT * FROM template_groups WHERE templateGroupId =:groupId")
    fun getGroupWithTemplatesObservable(groupId: Int): Flow<GroupWithTemplates>

    @Transaction
    @Query("SELECT * FROM template_groups WHERE templateGroupId =:groupId")
    fun getGroupWithTemplates(groupId: Int): GroupWithTemplates?

    @Transaction
    @Query("SELECT * FROM template_groups WHERE name =:name")
    fun getGroupWithTemplatesByName(name: String): GroupWithTemplates?

    @Query("DELETE FROM template_groups")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM template_groups")
    suspend fun count(): Int

}