package com.leviancode.android.gsmbox.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.leviancode.android.gsmbox.data.model.recipients.GroupWithRecipients
import com.leviancode.android.gsmbox.data.model.recipients.RecipientWithGroups
import com.leviancode.android.gsmbox.data.model.recipients.RecipientsAndGroupsCrossRef

@Dao
interface RecipientsAndGroupsCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: RecipientsAndGroupsCrossRef)

    @Delete
    suspend fun delete(item: RecipientsAndGroupsCrossRef)

    @Query("DELETE FROM recipients_and_groups WHERE recipientGroupId =:groupId")
    suspend fun deleteByGroupId(groupId: String)

    @Query("DELETE FROM recipients_and_groups WHERE recipientId =:recipientId")
    suspend fun deleteByRecipientId(recipientId: String)

    @Transaction
    @Query("SELECT * FROM recipients")
    fun getRecipientsWithGroups(): LiveData<List<RecipientWithGroups>>

    @Transaction
    @Query("SELECT * FROM recipient_groups")
    fun getGroupsWithRecipients(): LiveData<List<GroupWithRecipients>>

    @Transaction
    @Query("SELECT * FROM recipient_groups WHERE recipientGroupId =:groupId")
    suspend fun getGroupWithRecipients(groupId: String): GroupWithRecipients?

    @Transaction
    @Query("SELECT * FROM recipients WHERE recipientId = :recipientId")
    suspend fun getRecipientWithGroups(recipientId: String): RecipientWithGroups?
}