package com.brainymobile.android.smsbox.ui.dialogs.alertdialogs

import android.content.Context
import com.brainymobile.android.smsbox.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RecoveryAlertDialog (val context: Context) {
    fun show(callback: (Boolean) -> Unit){
        MaterialAlertDialogBuilder(context)
            .setIcon(R.drawable.ic_baseline_settings_backup_restore_24)
            .setTitle(context.getString(R.string.reсovery))
            .setMessage(context.getString(R.string.db_would_be_lost))
            .setNegativeButton(context.getString(R.string.cancel)){ dialog, _ ->
                dialog.dismiss()
            }.setPositiveButton(context.getString(R.string.restore)){ _, _ ->
                callback(true)
            }.show()
    }
}