package com.leviancode.android.gsmbox.core.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment

fun showKeyboard(view: View){
    val editText = view as EditText
    editText.requestFocus()

    val imm =
        view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val windowHeightMethod = InputMethodManager::class.java.getMethod("getInputMethodWindowVisibleHeight")
    val height = windowHeightMethod.invoke(imm) as Int
    if (height == 0) {
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}

fun hideKeyboard(view: View){
    val imm = view.context
        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.hideKeyboard(){
    val imm = requireContext()
        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val windowHeightMethod = InputMethodManager::class.java.getMethod("getInputMethodWindowVisibleHeight")
    val height = windowHeightMethod.invoke(imm) as Int
    if (height != 0) {
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }
   // imm.hideSoftInputFromWindow(requireView().windowToken, 0)
}