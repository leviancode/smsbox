package com.brainymobile.android.smsbox.data.database.dao.recipients

import androidx.room.*
import com.brainymobile.android.smsbox.data.entities.recipients.RecipientData
import com.brainymobile.android.smsbox.data.entities.recipients.RecipientWithGroupsData
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipientDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: RecipientData): Long

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<RecipientData>): LongArray

    suspend fun upsert(item: RecipientData): Int {
        return if (item.recipientId == 0) {
            val foundByNumber = getByPhoneNumber(item.phoneNumber)
            if (foundByNumber != null){
                item.recipientId = foundByNumber.recipientId
                item.name = foundByNumber.name ?: item.name
                update(item)
                item.recipientId
            } else {
                insert(item).toInt()
            }
        } else {
            update(item)
            item.recipientId
        }
    }

    @Update suspend fun update(vararg item: RecipientData)

    @Delete suspend fun delete(item: RecipientData)

    @Query("DELETE FROM recipients WHERE name is null AND recipients.recipientId NOT IN (SELECT recipients_and_groups.recipientId FROM recipients_and_groups)")
    suspend fun deleteUnused()

    @Query("SELECT * FROM recipients WHERE recipientId = :id")
    suspend fun get(id: Int): RecipientData?

    @Query("SELECT * FROM recipients WHERE name = :name")
    suspend fun getByName(name: String): RecipientData?

    @Query("SELECT * FROM recipients WHERE name is not null ORDER BY position")
    fun getAllObservable(): Flow<List<RecipientData>>

    @Query("SELECT * FROM recipients WHERE name is not null ORDER BY position")
    fun getAll(): List<RecipientData>

    @Query("SELECT phoneNumber FROM recipients WHERE name is not null")
    fun getNumbersWithNotEmptyNamesObservable(): Flow<List<String>>

    @Query("SELECT phoneNumber FROM recipients WHERE name is not null")
    suspend fun getNumbersWithNotEmptyNames(): List<String>

    @Transaction
    @Query("SELECT * FROM recipients WHERE name is not null ORDER BY position")
    fun getRecipientsWithGroupsObservable(): Flow<List<RecipientWithGroupsData>>

    @Transaction
    @Query("SELECT * FROM recipients WHERE name is not null ORDER BY position")
    suspend fun getRecipientsWithGroups(): List<RecipientWithGroupsData>

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

    @Query("SELECT COUNT(*) FROM recipients WHERE position is not -1")
    suspend fun count(): Int

}