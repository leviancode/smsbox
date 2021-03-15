package com.leviancode.android.gsmbox.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.leviancode.android.gsmbox.data.model.templates.TemplateGroup

@Dao
interface TemplateGroupDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg item: TemplateGroup)
    @Update suspend fun update(vararg item: TemplateGroup)
    @Delete suspend fun delete(vararg item: TemplateGroup)

    @Query("SELECT * from template_groups WHERE group_id = :id")
    suspend fun get(id: String): TemplateGroup?

    @Query("SELECT * FROM template_groups")
    fun getAllLiveData(): LiveData<List<TemplateGroup>>

    @Query("SELECT * FROM template_groups")
    suspend fun getAll(): List<TemplateGroup>

    @Query("DELETE FROM template_groups")
    suspend fun deleteAll()
}