package com.leviancode.android.gsmbox.ui.extra.alertdialogs

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.leviancode.android.gsmbox.R

object DeleteConfirmationAlertDialog {

    fun show(
        context: Context,
        title: String = context.getString(R.string.delete_item),
        msg: String = "",
        callback: (Boolean) -> Unit
    ) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(msg)
            .setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }.setPositiveButton(context.getString(R.string.delete)) { _, _ ->
                callback(true)
            }.show()
    }
}