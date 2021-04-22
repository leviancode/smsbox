package com.leviancode.android.gsmbox.data.dao.recipients

import androidx.lifecycle.LiveData
import androidx.room.*
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.data.model.recipients.GroupWithRecipients
import com.leviancode.android.gsmbox.data.model.recipients.RecipientsAndGroupRelation
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository

@Dao
interface RecipientGroupDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: RecipientGroup): Long

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<RecipientGroup>): LongArray

    suspend fun upsert(item: RecipientGroup): Int {
        return if (item.recipientGroupId == 0) insert(item).toInt()
        else {
            update(item)
            item.recipientGroupId
        }
    }
    @Update
    suspend fun update(item: RecipientGroup)

    @Delete
    suspend fun delete(vararg item: RecipientGroup)

    @Query("DELETE FROM recipient_groups WHERE recipientGroupId = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM recipient_groups WHERE name is null AND recipient_groups.recipientGroupId NOT IN (SELECT recipients_and_groups.recipientGroupId FROM recipients_and_groups)")
    suspend fun deleteUnused()

    @Query("SELECT * FROM recipient_groups WHERE recipientGroupId = :id")
    suspend fun getById(id: Int): RecipientGroup?

    @Query("SELECT * FROM recipient_groups WHERE recipientGroupId IN (:ids)")
    suspend fun getByIds(vararg ids: Int): List<RecipientGroup>

    @Query("SELECT * FROM recipient_groups WHERE name LIKE :name")
    suspend fun getByName(name: String): RecipientGroup?

    @Query("SELECT * FROM recipient_groups WHERE name is not null")
    fun getAllLiveData(): LiveData<List<RecipientGroup>>

    @Query("SELECT * FROM recipient_groups WHERE name is not null")
    fun getAll(): List<RecipientGroup>

    @Transaction
    @Query("SELECT * FROM recipient_groups  WHERE name is not null")
    fun getGroupsWithRecipients(): LiveData<List<GroupWithRecipients>>
    @Transaction
    @Query("SELECT * FROM recipient_groups WHERE recipientGroupId =:groupId")
    suspend fun getGroupWithRecipients(groupId: Int): GroupWithRecipients?


}