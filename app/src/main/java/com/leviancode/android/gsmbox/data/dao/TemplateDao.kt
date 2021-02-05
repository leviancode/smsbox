package com.leviancode.android.gsmbox.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.leviancode.android.gsmbox.data.model.Template

@Dao
interface TemplateDao {
    @Insert suspend fun insert(vararg template: Template)
    @Update suspend fun update(vararg template: Template)
    @Delete suspend fun delete(vararg template: Template)

    @Query("SELECT * from templates WHERE id = :id")
    suspend fun get(id: String): Template?

    @Query("SELECT * FROM templates")
    fun getAll(): LiveData<List<Template>>
}