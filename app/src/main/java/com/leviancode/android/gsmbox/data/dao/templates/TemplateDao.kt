package com.leviancode.android.gsmbox.data.dao.templates

import androidx.lifecycle.LiveData
import androidx.room.*
import com.leviancode.android.gsmbox.data.model.templates.Template
import com.leviancode.android.gsmbox.data.model.templates.TemplateWithRecipients
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository
import com.leviancode.android.gsmbox.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.utils.log

@Dao
interface TemplateDao {
    @Insert
    suspend fun insert(item: Template): Long

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Template>): LongArray

    suspend fun upsert(item: Template): Int {
        return if (item.templateId == 0) insert(item).toInt()
        else {
            update(item)
            item.templateId
        }
    }

    @Update suspend fun update(vararg item: Template)

    @Delete suspend fun delete(vararg item: Template)

    @Query("SELECT * from templates WHERE templateId = :id")
    suspend fun get(id: Int): Template?

    @Transaction
    @Query("SELECT * from templates WHERE templateId = :id")
    suspend fun getTemplateWithRecipients(id: Int): TemplateWithRecipients?

    @Transaction
    @Query("SELECT * from templates WHERE templateGroupId = :groupId")
    fun getTemplatesWithRecipients(groupId: Int): LiveData<List<TemplateWithRecipients>>

    @Transaction
    @Query("SELECT * from templates WHERE favorite = 1")
    fun getFavoriteWithRecipients(): LiveData<List<TemplateWithRecipients>>

    @Query("SELECT * from templates WHERE recipientGroupId = :id")
    suspend fun getByRecipientGroupId(id: Int): List<Template>

    @Query("SELECT * FROM templates")
    fun getAllLiveData(): LiveData<List<Template>>

    @Query("SELECT * FROM templates")
    fun getAll(): List<Template>

    @Query("SELECT * FROM templates WHERE templateGroupId = :id")
    fun getByGroupId(id: Int): LiveData<List<Template>>

    @Query("DELETE FROM templates WHERE templateId = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT name FROM templates")
    fun getTemplateNames(): LiveData<List<String>>

    @Query("SELECT name FROM templates WHERE templateId != :id")
    fun getTemplateNamesExclusiveById(id: Int): LiveData<List<String>>

    @Query("SELECT * FROM templates WHERE name LIKE :name")
    suspend fun getByName(name: String): Template?

}