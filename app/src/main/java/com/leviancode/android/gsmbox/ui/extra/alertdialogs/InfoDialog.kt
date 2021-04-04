package com.leviancode.android.gsmbox.ui.extra.alertdialogs

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.leviancode.android.gsmbox.R

object InfoDialog {
    fun show(context: Context, msg: String){
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.info))
            .setMessage(msg)
            .setPositiveButton(context.getString(R.string.ok)){ dialog, _ ->
                dialog.dismiss()
            }.show()
    }
}