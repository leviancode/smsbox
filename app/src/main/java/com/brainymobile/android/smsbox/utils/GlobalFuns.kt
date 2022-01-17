package com.brainymobile.android.smsbox.utils

import android.text.format.DateFormat
import android.util.Log
import java.util.*

fun logI(message: String){
    Log.i("SMSBOX", message)
}

inline fun <reified T : Any>logE(message: String){
    Log.e(T::class.java.name, message)
}

fun getFormatDate(): String = DateFormat.format(DATE_FORMAT, Date()).toString()

fun isNotNullOrEmpty(vararg strings: String?): Boolean = strings.count { !it.isNullOrBlank() } == strings.size

fun isEmpty(vararg strings: String): Boolean = strings.count { it.isBlank() } == strings.size