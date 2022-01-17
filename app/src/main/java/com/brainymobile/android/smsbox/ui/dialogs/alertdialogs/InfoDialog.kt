package com.brainymobile.android.smsbox.ui.dialogs.alertdialogs

import android.content.Context
import com.brainymobile.android.smsbox.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class InfoDialog(private val context: Context) {
    fun show(msg: String){
        MaterialAlertDialogBuilder(context)
            .setTitle(context.getString(R.string.info))
            .setMessage(msg)
            .setPositiveButton(context.getString(R.string.ok)){ dialog, _ ->
                dialog.dismiss()
            }.show()
    }
}