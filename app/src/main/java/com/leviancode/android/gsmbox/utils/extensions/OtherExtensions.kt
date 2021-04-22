package com.leviancode.android.gsmbox.utils.extensions

inline fun Boolean.ifTrue(action: () -> Unit){
    if (this) action()
}