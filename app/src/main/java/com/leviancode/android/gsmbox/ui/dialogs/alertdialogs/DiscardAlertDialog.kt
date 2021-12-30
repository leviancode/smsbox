package com.leviancode.android.gsmbox.ui.dialogs.alertdialogs

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.leviancode.android.gsmbox.R

class DiscardAlertDialog (private val context: Context)  {
    fun show(callback: (Boolean) -> Unit){
        MaterialAlertDialogBuilder(context)
            .setMessage(context.getString(R.string.discard_draft))
            .setNegativeButton(context.getString(R.string.cancel)){ dialog, _ ->
                dialog.dismiss()
            }.setPositiveButton(context.getString(R.string.discard)){ _, _ ->
                callback(true)
            }.show()
    }
}