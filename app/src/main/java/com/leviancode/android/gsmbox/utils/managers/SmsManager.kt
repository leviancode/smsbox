package com.leviancode.android.gsmbox.utils.managers

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.leviancode.android.gsmbox.data.model.templates.Template

object SmsManager {
    fun sendSms(context: Context, template: Template) {
        val addresses = template.getRecipients()
            .joinToString(";", "smsto:"){ it.getPhoneNumber() }

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(addresses)
        intent.putExtra(Intent.EXTRA_TEXT, template.getMessage())
        context.startActivity(intent)
    }
}