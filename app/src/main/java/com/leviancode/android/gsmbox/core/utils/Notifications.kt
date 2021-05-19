package com.leviancode.android.gsmbox.core.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.leviancode.android.gsmbox.R


fun showUndoSnackbar(view: View, message: String, callback: (View) -> Unit){
    Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        .setAction(view.context.getString(R.string.undo)) { callback(it) }
        .show()
}

fun showOpenSnackbar(view: View, message: String, callback: (View) -> Unit){
    Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        .setAction(view.context.getString(R.string.open)) { callback(it) }
        .show()
}