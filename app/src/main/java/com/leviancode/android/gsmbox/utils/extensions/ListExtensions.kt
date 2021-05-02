package com.leviancode.android.gsmbox.utils.extensions

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <T>List<T>.ifNotEmpty(defaultValue: (List<T>) -> Unit){
    if (isNotEmpty()) defaultValue(this)
}

inline fun <reified T>List<T>.toJson() = Json.encodeToString(this)

fun LongArray.toIntArray() = map { it.toInt() }.toIntArray()

fun List<String>.indexOfIgnoreCase(str: String): Int {
    for (i in this.indices){
        if (get(i).equals(str, true)) return i
    }
    return -1
}

