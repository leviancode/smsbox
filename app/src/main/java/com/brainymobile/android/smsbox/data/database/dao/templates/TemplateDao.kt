package com.brainymobile.android.smsbox.data.database.dao.templates

import androidx.room.*
import com.brainymobile.android.smsbox.data.entities.templates.template.TemplateData
import com.brainymobile.android.smsbox.data.entities.templates.template.TemplateWithRecipients
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

    @Update (onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(vararg item: TemplateData)

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
    @Query("SELECT * from templates WHERE templateGroupId = :groupId ORDER BY position")
    fun getTemplatesWithRecipients(groupId: Int): Flow<List<TemplateWithRecipients>>

    @Transaction
    @Query("SELECT * from templates WHERE favorite = 1 ORDER BY position")
    fun getFavoriteWithRecipientsObservable(): Flow<List<TemplateWithRecipients>>

    @Transaction
    @Query("SELECT * from templates WHERE favorite = 1")
    suspend fun getFavoriteWithRecipients(): List<TemplateWithRecipients>

    @Transaction
    @Query("SELECT * from templates WHERE favorite = 1 ORDER BY position")
    fun getFavoriteWithRecipientsSync(): List<TemplateWithRecipients>

    @Query("SELECT * FROM templates ORDER BY position")
    fun getAllObservable(): Flow<List<TemplateData>>

    @Query("SELECT * FROM templates ORDER BY position")
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

    @Query("SELECT COUNT(*) FROM templates")
    suspend fun count(): Int
}