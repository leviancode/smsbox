package com.brainymobile.android.smsbox.ui.dialogs.alertdialogs

import android.content.Context
import com.brainymobile.android.smsbox.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DeleteConfirmationAlertDialog(private val context: Context)  {

    fun show(
        title: String = context.getString(R.string.delete_item),
        msg: String = "",
        callback: (Boolean) -> Unit
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(msg)
            .setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }.setPositiveButton(context.getString(R.string.delete)) { _, _ ->
                callback(true)
            }.show()
    }
}