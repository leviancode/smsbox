package com.leviancode.android.gsmbox.utils

import android.text.format.DateFormat
import android.util.Log
import java.util.*

fun log(message: String){
    Log.i("SMSBOX", message)
}

fun getFormatDate(): String = DateFormat.format(DATE_FORMAT, Date()).toString()

fun isNotEmpty(vararg strings: String): Boolean = strings.count { it.isNotBlank() } == strings.size

fun isEmpty(vararg strings: String): Boolean = strings.count { it.isBlank() } == strings.size