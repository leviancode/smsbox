package com.leviancode.android.gsmbox.ui.view.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.leviancode.android.gsmbox.R

class DiscardDialog(val context: Context) {
    fun show(callback: (Boolean) -> Unit){
        AlertDialog.Builder(context)
            .setMessage(context.getString(R.string.discard_draft))
            .setNegativeButton(context.getString(R.string.cancel)){ dialog, _ ->
                dialog.dismiss()
            }.setPositiveButton(context.getString(R.string.discard)){ _, _ ->
                callback(true)
            }.show()
    }
}