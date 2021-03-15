package com.leviancode.android.gsmbox.data.repository

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.text.format.DateFormat.format
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.leviancode.android.gsmbox.helpers.BackupResult
import com.leviancode.android.gsmbox.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

object FilesRepository {
    private val db = AppDatabase.INSTANCE
    private val _result = MutableStateFlow(BackupResult.INIT)
    val result = _result.asStateFlow()

    suspend fun backup(context: Context) {
        _result.value = BackupResult.START
        try {
            val date = format(DATE_FORMAT, Date())
            val cacheDir = File(context.cacheDir, String.format(BACKUP_DIR, date))
            cacheDir.mkdirs()

            val files = listOf(
                db.templateGroupDao().getAll().toJson().toFile(cacheDir, TEMPLATE_GROUPS_FILE_NAME),
                db.templateDao().getAll().toJson().toFile(cacheDir, TEMPLATES_FILE_NAME),
                db.recipientDao().getAll().toJson().toFile(cacheDir, RECIPIENTS_FILE_NAME),
                db.recipientGroupDao().getAll().toJson()
                    .toFile(cacheDir, RECIPIENT_GROUPS_FILE_NAME)
            )
            val destFile = File(cacheDir, String.format(ZIP_DB_FILE_NAME, date))
            zipFiles(files, destFile)

            saveToDownloads(context, destFile)
            delay(600)
            _result.value = BackupResult.SUCCESS
        } catch (e: Exception) {
            log(e.stackTraceToString())
            _result.value = BackupResult.FAILED
        }

    }

    private fun saveToDownloads(context: Context, zipFile: File) {
        val resolver = context.contentResolver
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, zipFile.name)
                put(MediaStore.MediaColumns.MIME_TYPE, zipFile.getMimeType())
                put(MediaStore.MediaColumns.SIZE, zipFile.totalSpace)
            }
            resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        } else {
            val downloadsDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val destinyFile = File(downloadsDir, zipFile.name)
            FileProvider.getUriForFile(context, FILE_PROVIDER_AUTH, destinyFile)
        }?.also { destUri ->
            resolver.copyFile(zipFile.toUri(), destUri)
        }
    }

    suspend fun restore() = withContext(Dispatchers.IO) {

    }
}