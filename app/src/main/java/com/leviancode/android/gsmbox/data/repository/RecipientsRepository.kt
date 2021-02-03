package com.leviancode.android.gsmbox.repository

import com.leviancode.android.gsmbox.data.model.Recipient

object RecipientsRepository {
    val recipients = mutableListOf<Recipient>()


    fun addRecipient(recipient: Recipient){
        recipients.add(recipient)
    }

    fun getRecipientById(id: String): Recipient?{
        return recipients.find { it.id == id }
    }

    fun removeRecipient(recipient: Recipient) {
        recipients.remove(recipient)
    }
}