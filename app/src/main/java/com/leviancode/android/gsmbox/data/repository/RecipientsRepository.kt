package com.leviancode.android.gsmbox.data.repository

import androidx.lifecycle.LiveData
import com.leviancode.android.gsmbox.data.dao.AppDatabase
import com.leviancode.android.gsmbox.data.model.Recipient
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

object RecipientsRepository {
    private val dao = AppDatabase.INSTANCE!!.recipientDao()
    var data: LiveData<List<Recipient>> = dao.getAll()

    suspend fun saveRecipient(item: Recipient) = withContext(IO){
        val recipient = getRecipientById(item.recipientId)
        if (recipient == null) dao.insert(item)
        else dao.update(item)
    }

    suspend fun deleteRecipient(item: Recipient) = withContext(IO){
        dao.delete(item)
    }

    suspend fun getRecipientById(id: String) = withContext(IO){
        dao.get(id)
    }
}