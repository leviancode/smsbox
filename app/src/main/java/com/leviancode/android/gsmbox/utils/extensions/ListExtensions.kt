package com.leviancode.android.gsmbox.utils.extensions

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <T>List<T>.ifNotEmpty(defaultValue: (List<T>) -> Unit){
    if (isNotEmpty()) defaultValue(this)
}

inline fun <reified T>List<T>.toJson() = Json.encodeToString(this)
