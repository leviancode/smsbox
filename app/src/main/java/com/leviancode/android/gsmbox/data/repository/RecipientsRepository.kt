package com.leviancode.android.gsmbox.data.repository

import com.leviancode.android.gsmbox.data.model.Recipient

object RecipientsRepository {
    val recipients = mutableListOf<Recipient>()

    fun addOrUpdateRecipient(recipient: Recipient){
        val index = getRecipientIndex(recipient.id)
        if (index != -1){
            recipients[index] = recipient
        } else {
            recipients.add(recipient)
        }
    }

    fun getRecipientById(id: String): Recipient?{
        return recipients.find { it.id == id }
    }

    fun removeRecipient(id: String) {
        val index = getRecipientIndex(id)
        if (index != -1) recipients.removeAt(index)
    }

    private fun getRecipientIndex(id: String): Int {
        for (i in recipients.indices){
            if (recipients[i].id == id) return i
        }
        return -1
    }

    fun isUpdated(recipient: Recipient): Boolean {
        getRecipientById(recipient.id)?.let {
            return it.name != recipient.name || it.phoneNumber != recipient.phoneNumber
        }
        return true
    }
}