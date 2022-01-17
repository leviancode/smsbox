package com.brainymobile.android.smsbox.utils

import android.app.Activity
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment

fun View.showKeyboard(){
    ViewCompat.getWindowInsetsController(this)
        ?.show(WindowInsetsCompat.Type.ime())
    requestFocus()
}

fun View.hideKeyboard() = ViewCompat.getWindowInsetsController(this)
    ?.hide(WindowInsetsCompat.Type.ime())

fun Fragment.hideKeyboard() = ViewCompat.getWindowInsetsController(requireView())
    ?.hide(WindowInsetsCompat.Type.ime())

fun Fragment.showKeyboard() = ViewCompat.getWindowInsetsController(requireView())
    ?.show(WindowInsetsCompat.Type.ime())

fun Activity.showKeyboard(){
    WindowInsetsControllerCompat(window, window.decorView).show(WindowInsetsCompat.Type.ime())
}
fun Activity.hideKeyboard(){
    WindowInsetsControllerCompat(window, window.decorView).hide(WindowInsetsCompat.Type.ime())
}