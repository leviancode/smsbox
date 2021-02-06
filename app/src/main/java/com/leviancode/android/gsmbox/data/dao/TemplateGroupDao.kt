package com.leviancode.android.gsmbox.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.leviancode.android.gsmbox.data.model.Template
import com.leviancode.android.gsmbox.data.model.TemplateGroup

@Dao
interface TemplateGroupDao {
    @Insert suspend fun insert(vararg item: TemplateGroup)
    @Update suspend fun update(vararg item: TemplateGroup)
    @Delete suspend fun delete(vararg item: TemplateGroup)

    @Query("SELECT * from template_groups WHERE group_id = :id")
    suspend fun get(id: String): TemplateGroup?

    @Query("SELECT * FROM template_groups")
    fun getAll(): LiveData<List<TemplateGroup>>
}