package com.leviancode.android.gsmbox.core.utils.managers

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.leviancode.android.gsmbox.core.data.model.templates.TemplateWithRecipients
import com.leviancode.android.gsmbox.core.data.repository.PlaceholdersRepository
import com.leviancode.android.gsmbox.core.data.repository.TemplatesRepository
import com.leviancode.android.gsmbox.core.utils.extensions.hasPlaceholder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            this.data = Uri.parse(addresses)
            putExtra(Intent.EXTRA_TEXT, message)
        }
        context.startActivity(intent)
    }

    fun sendSms(context: Context, templateId: Int) {
        CoroutineScope(IO).launch{
            TemplatesRepository.getTemplateWithRecipients(templateId)?.let {
                withContext(Main){
                    sendSms(context, it)
                }
            }
        }

    }

    private suspend fun replacePlaceholders(message: String): String {
        return PlaceholdersRepository.replaceHashtagNamesToValues(message)
    }
}