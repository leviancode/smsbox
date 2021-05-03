package com.leviancode.android.gsmbox.utils.managers

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.leviancode.android.gsmbox.data.model.templates.TemplateWithRecipients
import com.leviancode.android.gsmbox.data.repository.PlaceholdersRepository
import com.leviancode.android.gsmbox.utils.extensions.hasPlaceholder


object SmsManager {
    suspend fun sendSms(context: Context, template: TemplateWithRecipients) {
        if (template.recipients == null) return

        var message = template.template.getMessage()
        if (message.hasPlaceholder()){
            message = replacePlaceholders(message)
        }
        val addresses =
            template.recipients!!.getRecipients().joinToString(";", "smsto:") { it.getPhoneNumber() }

        val intent = Intent(Intent.ACTION_VIEW).apply {
            this.data = Uri.parse(addresses)
            putExtra(Intent.EXTRA_TEXT, message)
        }
        context.startActivity(intent)
    }

    private suspend fun replacePlaceholders(message: String): String {
        return PlaceholdersRepository.replaceHashtagNamesToValues(message)
    }
}