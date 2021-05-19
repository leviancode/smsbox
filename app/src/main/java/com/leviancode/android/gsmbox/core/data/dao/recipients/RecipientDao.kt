package com.leviancode.android.gsmbox.core.data.dao.recipients

import androidx.lifecycle.LiveData
import androidx.room.*
import com.leviancode.android.gsmbox.core.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.core.data.model.recipients.RecipientWithGroups

@Dao
interface RecipientDao {
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Recipient): Long

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Recipient>): LongArray

    suspend fun upsert(item: Recipient): Int {
        return if (item.recipientId == 0) insert(item).toInt()
        else {
            update(item)
            item.recipientId
        }
    }

    @Update
    suspend fun update(vararg item: Recipient)

    @Delete
    suspend fun delete(item: Recipient)

    @Query("DELETE FROM recipients WHERE name is null AND recipients.recipientId NOT IN (SELECT recipients_and_groups.recipientId FROM recipients_and_groups)")
    suspend fun deleteUnused()

    @Query("SELECT * FROM recipients WHERE recipientId = :id")
    suspend fun get(id: Int): Recipient?

    @Query("SELECT * FROM recipients WHERE name is not null AND name LIKE :name")
    suspend fun getByName(name: String): Recipient?

    @Query("SELECT * FROM recipients WHERE name is not null")
    fun getAllLiveData(): LiveData<List<Recipient>>

    @Query("SELECT * FROM recipients WHERE name is not null")
    fun getAll(): List<Recipient>

    @Query("SELECT phoneNumber FROM recipients WHERE name is not null")
    fun getNumbersWithNotEmptyNamesLiveData(): LiveData<List<String>>

    @Query("SELECT phoneNumber FROM recipients WHERE name is not null")
    suspend fun getNumbersWithNotEmptyNames(): List<String>

    @Transaction
    @Query("SELECT * FROM recipients WHERE name is not null")
    fun getRecipientsWithGroups(): LiveData<List<RecipientWithGroups>>

    @Transaction
    @Query("SELECT * FROM recipients WHERE recipientId = :recipientId")
    suspend fun getRecipientWithGroups(recipientId: Int): RecipientWithGroups?

    @Query("SELECT * FROM recipients WHERE phoneNumber = :phoneNumber")
    suspend fun getByPhoneNumber(phoneNumber: String): Recipient?



}