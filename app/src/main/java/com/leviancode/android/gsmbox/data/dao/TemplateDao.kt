package com.leviancode.android.gsmbox.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.leviancode.android.gsmbox.data.model.templates.Template
import com.leviancode.android.gsmbox.data.model.templates.TemplateWithRecipients

@Dao
interface TemplateDao {
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Template): Long
    @Update suspend fun update(item: Template)

    @Delete suspend fun delete(vararg item: Template)

    @Query("SELECT * from templates WHERE templateId = :id")
    suspend fun get(id: Long): Template?

    @Transaction
    @Query("SELECT * from templates WHERE templateId = :id")
    suspend fun getTemplateWithRecipients(id: Long): TemplateWithRecipients?

    @Transaction
    @Query("SELECT * from templates WHERE templateGroupId = :groupId")
    fun getTemplatesWithRecipients(groupId: Long): LiveData<List<TemplateWithRecipients>>

    @Transaction
    @Query("SELECT * from templates WHERE favorite = 1")
    fun getFavoriteWithRecipients(): LiveData<List<TemplateWithRecipients>>

    @Query("SELECT * from templates WHERE recipientGroupId = :id")
    suspend fun getByRecipientGroupId(id: Long): List<Template>

    @Query("SELECT * FROM templates")
    fun getAllLiveData(): LiveData<List<Template>>

    @Query("SELECT * FROM templates")
    fun getAll(): List<Template>

    @Query("SELECT * FROM templates WHERE templateGroupId = :id")
    fun getByGroupId(id: Long): LiveData<List<Template>>

    @Query("DELETE FROM templates WHERE templateId = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT name FROM templates")
    fun getTemplateNames(): LiveData<List<String>>

    @Query("SELECT name FROM templates WHERE templateId != :id")
    fun getTemplateNamesExclusiveById(id: Long): LiveData<List<String>>

    @Query("SELECT * FROM templates WHERE name = :name")
    suspend fun getByName(name: String): Template?

}