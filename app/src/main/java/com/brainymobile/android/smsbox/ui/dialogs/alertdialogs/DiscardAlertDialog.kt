package com.brainymobile.android.smsbox.ui.dialogs.alertdialogs

import android.content.Context
import com.brainymobile.android.smsbox.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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