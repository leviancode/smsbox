package com.leviancode.android.gsmbox.ui.recipients.viewmodel

import com.leviancode.android.gsmbox.data.model.Recipient
import com.leviancode.android.gsmbox.data.model.RecipientObservable
import com.leviancode.android.gsmbox.data.repository.RecipientsRepository

class RecipientViewModel {
    private val repository = RecipientsRepository
    var recipient = RecipientObservable()

    fun setData(data: Recipient) {
        recipient.data = data
    }
}