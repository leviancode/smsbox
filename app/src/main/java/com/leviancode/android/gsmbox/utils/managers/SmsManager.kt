package com.leviancode.android.gsmbox.utils.managers

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.leviancode.android.gsmbox.data.model.templates.TemplateWithRecipients
import com.leviancode.android.gsmbox.data.repository.PlaceholdersRepository

object SmsManager {
    suspend fun sendSms(context: Context, data: TemplateWithRecipients) {
        var message = data.template.getMessage()
        if (hasPlaceholder(message)){
            message = replacePlaceholder(message)
        }
        val addresses = data.recipients.getRecipients().map { it.getPhoneNumber() }
            .joinToString(";", "smsto:")

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(addresses)
        intent.putExtra(Intent.EXTRA_TEXT, message)
        context.startActivity(intent)
    }

    private suspend fun replacePlaceholder(message: String): String {
        val name = "#" + message.substringAfter('#').substringBefore(" ")
        return PlaceholdersRepository.getValueByName(name)?.let { value ->
            message.replace(name, value)
        } ?: message
    }

    private fun hasPlaceholder(message: String) = message.contains('#')
}