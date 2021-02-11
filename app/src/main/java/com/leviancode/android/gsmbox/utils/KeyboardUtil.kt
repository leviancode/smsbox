package com.leviancode.android.gsmbox.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardUtil {
    fun showKeyboard(view: View){
        view.requestFocus()
        val imm =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val windowHeightMethod = InputMethodManager::class.java.getMethod("getInputMethodWindowVisibleHeight")
        val height = windowHeightMethod.invoke(imm) as Int
        if (height == 0) imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun hideKeyboard(view: View){
        val imm = view.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}