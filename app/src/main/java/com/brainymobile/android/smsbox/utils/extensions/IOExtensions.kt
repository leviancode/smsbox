package com.brainymobile.android.smsbox.utils.extensions

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import okio.ByteString.Companion.readByteString
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.charset.Charset
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

fun Uri.isValidSQLite(context: Context): Boolean {
    context.contentResolver.openInputStream(this)?.use { stream ->
        val str = stream.readByteString(16).string(Charset.defaultCharset())
        return str == "SQLite format 3\u0000"
    }
    return false
}

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

fun List<File>.zipFiles(destFile: File) {
    ZipOutputStream(FileOutputStream(destFile)).use {
        forEach { file ->
            val fis = FileInputStream(file)
            val zipEntry = ZipEntry(file.name)
            it.putNextEntry(zipEntry)
            val bytes = ByteArray(1024)
            var length: Int
            while (fis.read(bytes).also { length = it } >= 0) {
                it.write(bytes, 0, length)
            }
            fis.close()
        }
    }
}