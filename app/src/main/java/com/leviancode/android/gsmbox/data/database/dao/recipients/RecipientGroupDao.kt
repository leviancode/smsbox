package com.leviancode.android.gsmbox.data.database.dao.recipients

import androidx.room.*
import com.leviancode.android.gsmbox.data.entities.recipients.RecipientGroupData
import com.leviancode.android.gsmbox.data.entities.recipients.GroupWithRecipients
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipientGroupDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: RecipientGroupData): Long

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<RecipientGroupData>): LongArray

    suspend fun upsert(item: RecipientGroupData): Int {
        return if (item.recipientGroupId == 0) insert(item).toInt()
        else {
            update(item)
            item.recipientGroupId
        }
    }
    @Update (onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: RecipientGroupData)

    @Delete
    suspend fun delete(vararg item: RecipientGroupData)

    @Query("DELETE FROM recipient_groups WHERE recipientGroupId = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM recipient_groups WHERE name is null AND recipient_groups.recipientGroupId NOT IN (SELECT recipients_and_groups.recipientGroupId FROM recipients_and_groups)")
    suspend fun deleteUnused()

    @Query("SELECT * FROM recipient_groups WHERE recipientGroupId = :id")
    suspend fun getById(id: Int): RecipientGroupData?

    @Query("SELECT * FROM recipient_groups WHERE recipientGroupId IN (:ids) ORDER BY position")
    suspend fun getByIds(vararg ids: Int): List<RecipientGroupData>

    @Query("SELECT * FROM recipient_groups WHERE name LIKE :name")
    suspend fun getByName(name: String): RecipientGroupData?

    @Transaction
    @Query("SELECT * FROM recipient_groups WHERE name LIKE :name")
    suspend fun getGroupsWithRecipientsByName(name: String): GroupWithRecipients?

    @Query("SELECT * FROM recipient_groups WHERE name is not null")
    fun getAllObservable(): Flow<List<RecipientGroupData>>

    @Query("SELECT * FROM recipient_groups WHERE name is not null")
    fun getAll(): List<RecipientGroupData>

    @Transaction
    @Query("SELECT * FROM recipient_groups  WHERE name is not null ORDER BY position")
    fun getGroupsWithRecipientsObservable(): Flow<List<GroupWithRecipients>>

    @Transaction
    @Query("SELECT * FROM recipient_groups  WHERE name is not null ORDER BY position")
    suspend fun getGroupsWithRecipients(): List<GroupWithRecipients>

    @Transaction
    @Query("SELECT * FROM recipient_groups WHERE recipientGroupId =:groupId")
    suspend fun getGroupWithRecipients(groupId: Int): GroupWithRecipients?


}