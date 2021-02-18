package com.leviancode.android.gsmbox.utils

import android.content.Context
import android.view.Gravity
import android.widget.Toast


fun showToast(context: Context, message: String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 180)
    }.show()
}