package com.leviancode.android.gsmbox.ui.extra

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.leviancode.android.gsmbox.R

class DeleteConfirmationDialog(val context: Context) {
    var title = context.getString(R.string.delete_item)
    var message = ""

    fun show(callback: (Boolean) -> Unit){
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(context.getString(R.string.cancel)){ dialog, _ ->
                dialog.dismiss()
            }.setPositiveButton(context.getString(R.string.delete)){ _, _ ->
                callback(true)
            }.show()
    }
}