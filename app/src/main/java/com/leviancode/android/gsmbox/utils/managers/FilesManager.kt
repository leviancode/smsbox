package com.leviancode.android.gsmbox.utils.managers

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.leviancode.android.gsmbox.utils.FILE_PROVIDER_AUTH
import com.leviancode.android.gsmbox.utils.extensions.copyFile
import com.leviancode.android.gsmbox.utils.extensions.getMimeType
import java.io.File

class FilesManager(private val context: Context) {

    fun saveToDownloads(file: File, fileName: String): Uri? {
        val resolver = context.contentResolver
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, file.getMimeType())
                put(MediaStore.MediaColumns.SIZE, file.totalSpace)
            }
            resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        } else {
            val downloadsDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val destFile = File(downloadsDir, fileName)

            FileProvider.getUriForFile(context, FILE_PROVIDER_AUTH, destFile)
        }?.also { destUri ->
            resolver.copyFile(file.toUri(), destUri)
        }
    }

    fun createCacheDir(dirName: String): File {
        val cacheDir = File(context.cacheDir, dirName)
        cacheDir.mkdirs()
        return cacheDir
    }
}