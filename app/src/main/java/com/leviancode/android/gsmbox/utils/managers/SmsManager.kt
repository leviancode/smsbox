package com.leviancode.android.gsmbox.utils.managers

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.leviancode.android.gsmbox.data.model.templates.Template
import com.leviancode.android.gsmbox.data.repository.PlaceholdersRepository

object SmsManager {
    suspend fun sendSms(context: Context, template: Template) {
        var message = template.getMessage()
        if (hasPlaceholder(message)){
            message = replacePlaceholder(message)
        }
        val addresses = template.getRecipients()
            .joinToString(";", "smsto:"){ it.getPhoneNumber() }

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(addresses)
        intent.putExtra(Intent.EXTRA_TEXT, message)
        context.startActivity(intent)
    }

    private suspend fun replacePlaceholder(message: String): String {
        val key = "#" + message.substringAfter('#').substringBefore(" ")
        return PlaceholdersRepository.getValueByName(key)?.let { value ->
            message.replace(key, value)
        } ?: message
    }

    private fun hasPlaceholder(message: String) = message.contains('#')
}