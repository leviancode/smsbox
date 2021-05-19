package com.leviancode.android.gsmbox.core.utils.extensions

inline fun Boolean.ifTrue(action: () -> Unit){
    if (this) action()
}
