package com.leviancode.android.gsmbox.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.leviancode.android.gsmbox.data.model.Template

object SmsManager {
    fun sendSms(context: Context, template: Template) {
        val addresses = template.recipients
            .joinToString(";", "smsto:")

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(addresses)
        intent.putExtra(Intent.EXTRA_TEXT, template.message)
        context.startActivity(intent)
    }
}