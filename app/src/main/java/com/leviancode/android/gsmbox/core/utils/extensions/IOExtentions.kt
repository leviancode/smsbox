package com.leviancode.android.gsmbox.core.utils.extensions

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import okio.ByteString.Companion.readByteString
import java.io.File
import java.nio.charset.Charset
import java.util.*

fun Uri.isValidSQLite(context: Context): Boolean {
    context.contentResolver.openInputStream(this)?.use { stream ->
        val str = stream.readByteString(16).string(Charset.defaultCharset())
        return str == "SQLite format 3\u0000"
    }
    return false
}

/*fun File.copyTo(file: File) {
    inputStream().use { input ->
        file.outputStream().use { output ->
            input.copyTo(output)
        }
    }
}*/

fun File.copyTo(resolver: ContentResolver, uri: Uri) {
    inputStream().use { input ->
        resolver.openOutputStream(uri)?.use { output ->
            input.copyTo(output)
        }
    }
}

fun ContentResolver.copyFile(from: Uri, to: Uri) {
    openInputStream(from)?.use { input ->
        openOutputStream(to)?.use { output ->
            input.copyTo(output)
        }
    }
}

fun File.copyTo(dest: File) {
    dest.writeBytes(readBytes())
}

fun File.getMimeType(fallback: String = "image/*"): String {
    return MimeTypeMap.getFileExtensionFromUrl(toString())
        ?.run { MimeTypeMap.getSingleton().getMimeTypeFromExtension(toLowerCase(Locale.ROOT)) }
        ?: fallback // You might set it to */*
}