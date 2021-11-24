package com.leviancode.android.gsmbox.ui.dialogs.alertdialogs

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.leviancode.android.gsmbox.R

class InfoDialog(private val context: Context) {
    fun show(msg: String){
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.info))
            .setMessage(msg)
            .setPositiveButton(context.getString(R.string.ok)){ dialog, _ ->
                dialog.dismiss()
            }.show()
    }
}