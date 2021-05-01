package com.leviancode.android.gsmbox.utils.managers

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.leviancode.android.gsmbox.data.model.templates.TemplateWithRecipients
import com.leviancode.android.gsmbox.data.repository.PlaceholdersRepository
import com.leviancode.android.gsmbox.utils.HASHTAG_REGEX
import com.leviancode.android.gsmbox.utils.extensions.replaceHashtag
import java.util.regex.Pattern

object SmsManager {
    suspend fun sendSms(context: Context, data: TemplateWithRecipients) {
        if (data.recipients == null) return

        var message = data.template.getMessage()
        if (hasPlaceholder(message)){
            message = replacePlaceholders(message)
        }
        val addresses =
            data.recipients!!.getRecipients().joinToString(";", "smsto:") { it.getPhoneNumber() }

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(addresses)
        intent.putExtra(Intent.EXTRA_TEXT, message)
        context.startActivity(intent)
    }

    private suspend fun replacePlaceholders(message: String): String {
        return message.replaceHashtag { tag, startIndex, endIndex ->
            PlaceholdersRepository.getValueByName(tag) ?: tag
        }
    }

    private fun hasPlaceholder(message: String) = message.contains('#')
}