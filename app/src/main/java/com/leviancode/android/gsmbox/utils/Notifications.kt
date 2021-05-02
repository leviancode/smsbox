package com.leviancode.android.gsmbox.utils

import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
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