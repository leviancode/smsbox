package com.leviancode.android.gsmbox.utils

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.leviancode.android.gsmbox.R


fun showToast(context: Context, message: String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 180)
    }.show()
}

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