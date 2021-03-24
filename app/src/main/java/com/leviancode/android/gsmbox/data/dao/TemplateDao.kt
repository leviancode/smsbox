package com.leviancode.android.gsmbox.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.leviancode.android.gsmbox.data.model.templates.Template

@Dao
interface TemplateDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg item: Template)
    @Update suspend fun update(vararg item: Template)
    @Delete suspend fun delete(vararg item: Template)

    @Query("SELECT * from templates WHERE templateId = :id")
    suspend fun get(id: String): Template?

    @Query("SELECT * from templates WHERE recipientGroupId = :id")
    suspend fun getByRecipientGroupId(id: String): List<Template>

    @Query("SELECT * FROM templates")
    fun getAllLiveData(): LiveData<List<Template>>

    @Query("SELECT * FROM templates")
    fun getAll(): List<Template>

    @Query("SELECT * FROM templates WHERE templateGroupId = :id")
    fun getByGroupId(id: String): LiveData<List<Template>>

    @Query("DELETE FROM templates WHERE templateId = :id")
    suspend fun deleteById(id: String)
}