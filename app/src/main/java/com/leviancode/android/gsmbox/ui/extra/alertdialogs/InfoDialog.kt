package com.leviancode.android.gsmbox.ui.extra.alertdialogs

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.leviancode.android.gsmbox.R

object InfoDialog {
    fun show(context: Context, msg: String){
        AlertDialog.Builder(context)
            .setIcon(R.drawable.ic_outline_info_24)
            .setTitle(context.getString(R.string.info))
            .setMessage(msg)
            .setPositiveButton(context.getString(R.string.ok)){ dialog, _ ->
                dialog.dismiss()
            }.show()
    }
}