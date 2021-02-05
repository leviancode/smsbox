package com.leviancode.android.gsmbox.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leviancode.android.gsmbox.data.model.Recipient
import com.leviancode.android.gsmbox.utils.addItem
import com.leviancode.android.gsmbox.utils.removeItem
import com.leviancode.android.gsmbox.utils.updateItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object RecipientsRepository {
    private var _recipients = MutableLiveData<List<Recipient>>()
    var recipients: LiveData<List<Recipient>> = _recipients

    init {
        loadDataFromDb()
    }

    private fun loadDataFromDb(){
        CoroutineScope(Dispatchers.Main).launch {
            val data = async(Dispatchers.IO) { loadRecipients() }
            _recipients.value = data.await()
        }
    }

    private fun loadRecipients(): List<Recipient>{
        val result = mutableListOf<Recipient>()

        return result
    }

    fun addRecipient(item: Recipient){
        val index = getRecipientIndex(item.id)
        if (index != -1) _recipients.updateItem(index, item)
        else _recipients.addItem(item)
    }

    fun updateRecipient(item: Recipient){
        _recipients.updateItem(getRecipientIndex(item.id), item)
    }

    fun removeRecipient(id: String) {
        getRecipientById(id)?.let { _recipients.removeItem(it) }
    }

    fun getRecipientById(id: String): Recipient?{
        recipients.value?.let { list ->
            return list.find { it.id == id }
        }
        return null
    }

    private fun getRecipientIndex(id: String): Int {
        return recipients.value?.let { list -> list.indexOfFirst { it.id == id } } ?: -1
    }

    fun isUpdated(recipient: Recipient): Boolean {
        getRecipientById(recipient.id)?.let {
            return it.name != recipient.name || it.phoneNumber != recipient.phoneNumber
        }
        return true
    }
}