package com.leviancode.android.gsmbox.ui.dialogs.alertdialogs

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.leviancode.android.gsmbox.R

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