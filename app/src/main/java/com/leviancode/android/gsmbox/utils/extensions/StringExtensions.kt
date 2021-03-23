package com.leviancode.android.gsmbox.utils.extensions

import java.io.File

fun String.toFile(dir: String, fileName: String): File {
    val file = File(dir, fileName)
    file.writeText(this)
    return file
}
fun String.toFile(dir: File, fileName: String): File {
    val file = File(dir, fileName)
    file.writeText(this)
    return file
}
