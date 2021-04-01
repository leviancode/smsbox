package com.leviancode.android.gsmbox.ui.extra.alertdialogs

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.leviancode.android.gsmbox.R

object DiscardAlertDialog {
    fun show(context: Context, callback: (Boolean) -> Unit){
        AlertDialog.Builder(context)
            .setMessage(context.getString(R.string.discard_draft))
            .setNegativeButton(context.getString(R.string.cancel)){ dialog, _ ->
                dialog.dismiss()
            }.setPositiveButton(context.getString(R.string.discard)){ _, _ ->
                callback(true)
            }.show()
    }
}