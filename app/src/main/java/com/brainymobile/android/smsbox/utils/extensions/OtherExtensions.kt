package com.brainymobile.android.smsbox.utils.extensions

inline fun Boolean.ifTrue(action: () -> Unit){
    if (this) action()
}
