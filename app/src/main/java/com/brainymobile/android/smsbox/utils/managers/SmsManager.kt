package com.brainymobile.android.smsbox.utils.managers

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.brainymobile.android.smsbox.domain.usecases.placeholders.ReplacePlaceholdersUseCase
import com.brainymobile.android.smsbox.domain.usecases.templates.tempates.FetchTemplatesUseCase
import com.brainymobile.android.smsbox.ui.entities.templates.TemplateUI
import com.brainymobile.android.smsbox.ui.entities.templates.toTemplateUI
import com.brainymobile.android.smsbox.utils.extensions.hasPlaceholder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SmsManager(
    private val fetchTemplatesUseCase: FetchTemplatesUseCase,
    private val replacePlaceholdersUseCase: ReplacePlaceholdersUseCase
) {

    suspend fun sendSms(context: Context, template: TemplateUI) {
        val recipients = template.recipientGroup.recipients
        if (recipients.isEmpty()) return

        var message = template.getMessage()
        if (message.hasPlaceholder()){
            message = replacePlaceholders(message)
        }
        val addresses =
            recipients.joinToString(";", "smsto:") { it.getPhoneNumber() }

        val intent = Intent(Intent.ACTION_VIEW).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            this.data = Uri.parse(addresses)
            putExtra(Intent.EXTRA_TEXT, message)
        }
        context.startActivity(intent)
    }

    fun sendSms(context: Context, templateId: Int) {
        CoroutineScope(IO).launch{
            fetchTemplatesUseCase.get(templateId)?.let {
                withContext(Main){
                    sendSms(context, it.toTemplateUI())
                }
            }
        }

    }

    private suspend fun replacePlaceholders(message: String): String {
        return replacePlaceholdersUseCase.replace(message)
    }
}