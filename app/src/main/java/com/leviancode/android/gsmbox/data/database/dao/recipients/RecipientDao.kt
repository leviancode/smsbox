package com.leviancode.android.gsmbox.data.database.dao.recipients

import androidx.room.*
import com.leviancode.android.gsmbox.data.entities.recipients.RecipientData
import com.leviancode.android.gsmbox.data.entities.recipients.RecipientWithGroupsData
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipientDao {
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: RecipientData): Long

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<RecipientData>): LongArray

    suspend fun upsert(item: RecipientData): Int {
        return if (item.recipientId == 0) insert(item).toInt()
        else {
            update(item)
            item.recipientId
        }
    }

    suspend fun upsert(items: List<RecipientData>) {
        items.forEach { item ->
            if (item.recipientId == 0) insert(item)
            else {
                update(item)
                item.recipientId
            }
        }

    }

    @Update
    suspend fun update(vararg item: RecipientData)

    @Delete
    suspend fun delete(item: RecipientData)

    @Query("DELETE FROM recipients WHERE name is null AND recipients.recipientId NOT IN (SELECT recipients_and_groups.recipientId FROM recipients_and_groups)")
    suspend fun deleteUnused()

    @Query("SELECT * FROM recipients WHERE recipientId = :id")
    suspend fun get(id: Int): RecipientData?

    @Query("SELECT * FROM recipients WHERE name is not null AND name LIKE :name")
    suspend fun getByName(name: String): RecipientData?

    @Query("SELECT * FROM recipients WHERE name is not null")
    fun getAllObservable(): Flow<List<RecipientData>>

    @Query("SELECT * FROM recipients WHERE name is not null")
    fun getAll(): List<RecipientData>

    @Query("SELECT phoneNumber FROM recipients WHERE name is not null")
    fun getNumbersWithNotEmptyNamesObservable(): Flow<List<String>>

    @Query("SELECT phoneNumber FROM recipients WHERE name is not null")
    suspend fun getNumbersWithNotEmptyNames(): List<String>

    @Transaction
    @Query("SELECT * FROM recipients WHERE name is not null")
    fun getRecipientsWithGroupsObservable(): Flow<List<RecipientWithGroupsData>>

    @Transaction
    @Query("SELECT * FROM recipients WHERE name is not null")
    fun getRecipientsWithGroups(): List<RecipientWithGroupsData>

    @Transaction
    @Query("SELECT * FROM recipients WHERE recipientId = :id")
    suspend fun getRecipientWithGroupsById(id: Int): RecipientWithGroupsData?

    @Transaction
    @Query("SELECT * FROM recipients WHERE name = :name")
    suspend fun getRecipientWithGroupsByName(name: String): RecipientWithGroupsData?

    @Transaction
    @Query("SELECT * FROM recipients WHERE phoneNumber = :phoneNumber")
    suspend fun getRecipientWithGroupsByPhoneNumber(phoneNumber: String): RecipientWithGroupsData?

    @Query("SELECT * FROM recipients WHERE phoneNumber = :phoneNumber")
    suspend fun getByPhoneNumber(phoneNumber: String): RecipientData?

}