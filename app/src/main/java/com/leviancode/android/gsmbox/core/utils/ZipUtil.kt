package com.leviancode.android.gsmbox.core.utils

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

fun zipFiles(srcFiles: List<File>, destFile: File) {
    ZipOutputStream(FileOutputStream(destFile)).use {
        for (file in srcFiles) {
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