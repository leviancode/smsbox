package com.leviancode.android.gsmbox.data.dao

import androidx.room.*
import com.leviancode.android.gsmbox.data.model.TemplateGroup

@Dao
interface TemplateGroupDao {
    @Insert
    fun insert(vararg template: TemplateGroup)
    @Update
    fun update(vararg template: TemplateGroup)
    @Delete
    fun delete(vararg template: TemplateGroup)

    @Query("SELECT * FROM template_groups")
    fun getAll(): List<TemplateGroup>
}