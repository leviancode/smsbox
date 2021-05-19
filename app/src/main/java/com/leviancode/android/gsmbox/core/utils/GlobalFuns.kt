package com.leviancode.android.gsmbox.core.utils

import android.text.format.DateFormat
import android.util.Log
import java.util.*

fun log(message: String){
    Log.i("SMSBOX", message)
}

fun getFormatDate(): String = DateFormat.format(DATE_FORMAT, Date()).toString()

fun isNotNullOrEmpty(vararg strings: String?): Boolean = strings.count { !it.isNullOrBlank() } == strings.size

fun isEmpty(vararg strings: String): Boolean = strings.count { it.isBlank() } == strings.size