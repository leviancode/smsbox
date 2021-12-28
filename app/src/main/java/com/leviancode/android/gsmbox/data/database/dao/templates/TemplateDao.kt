package com.leviancode.android.gsmbox.data.database.dao.templates

import androidx.room.*
import com.leviancode.android.gsmbox.data.entities.templates.template.TemplateData
import com.leviancode.android.gsmbox.data.entities.templates.template.TemplateWithRecipients
import kotlinx.coroutines.flow.Flow

@Dao
interface TemplateDao {
    @Insert
    suspend fun insert(item: TemplateData): Long

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<TemplateData>): LongArray

    suspend fun upsert(item: TemplateData): Int {
        return if (item.templateId == 0) insert(item).toInt()
        else {
            update(item)
            item.templateId
        }
    }

    @Update suspend fun update(vararg item: TemplateData): Int

    @Query("UPDATE templates SET favorite =:favorite WHERE templateId =:id")
    suspend fun updateFavorite(id: Int, favorite: Boolean): Int

    @Delete suspend fun delete(vararg item: TemplateData)

    @Query("SELECT * from templates WHERE templateId = :id")
    suspend fun get(id: Int): TemplateData?

    @Transaction
    @Query("SELECT * from templates WHERE templateId = :id")
    suspend fun getTemplateWithRecipientsById(id: Int): TemplateWithRecipients?

    @Transaction
    @Query("SELECT * from templates WHERE name = :name")
    suspend fun getTemplateWithRecipientsByName(name: String): TemplateWithRecipients?

    @Transaction
    @Query("SELECT * from templates WHERE templateGroupId = :groupId")
    fun getTemplatesWithRecipients(groupId: Int): Flow<List<TemplateWithRecipients>>

    @Transaction
    @Query("SELECT * from templates WHERE favorite = 1")
    fun getFavoriteWithRecipientsObservable(): Flow<List<TemplateWithRecipients>>

    @Transaction
    @Query("SELECT * from templates WHERE favorite = 1")
    suspend fun getFavoriteWithRecipients(): List<TemplateWithRecipients>

    @Transaction
    @Query("SELECT * from templates WHERE favorite = 1")
    fun getFavoriteWithRecipientsSync(): List<TemplateWithRecipients>

    @Query("SELECT * from templates WHERE recipientGroupId = :id")
    suspend fun getByRecipientGroupId(id: Int): List<TemplateData>

    @Query("SELECT * FROM templates")
    fun getAllObservable(): Flow<List<TemplateData>>

    @Query("SELECT * FROM templates")
    fun getAll(): List<TemplateData>

    @Query("SELECT * FROM templates WHERE templateGroupId = :id")
    fun getByGroupId(id: Int): Flow<List<TemplateData>>

    @Query("DELETE FROM templates WHERE templateId = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM templates WHERE templateGroupId = :groupId")
    suspend fun deleteByGroupId(groupId: Int)

    @Query("SELECT name FROM templates")
    fun getTemplateNames(): Flow<List<String>>

    @Query("SELECT name FROM templates WHERE templateId != :id")
    fun getTemplateNamesExclusiveById(id: Int): Flow<List<String>>

    @Query("SELECT * FROM templates WHERE name LIKE :name")
    suspend fun getByName(name: String): TemplateData?

}