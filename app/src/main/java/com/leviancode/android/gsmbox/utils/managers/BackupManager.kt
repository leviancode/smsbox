package com.leviancode.android.gsmbox.utils.managers

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import com.leviancode.android.gsmbox.data.repository.AppDatabase
import com.leviancode.android.gsmbox.utils.helpers.BackupResult
import com.leviancode.android.gsmbox.utils.*
import com.leviancode.android.gsmbox.utils.extensions.*
import kotlinx.coroutines.delay
import java.io.File

object BackupManager {
    private val db = AppDatabase.INSTANCE
    private val _backupResult = SingleLiveEvent<BackupResult>()
    val backupResult: LiveData<BackupResult> = _backupResult
    private val _restoreResult = SingleLiveEvent<BackupResult>()
    val restoreResult: LiveData<BackupResult> = _restoreResult

    suspend fun backupToJson(context: Context) {
        _backupResult.postValue(BackupResult.START)
        try {
            val cacheDir = File(context.cacheDir, BACKUP_DIR)
            cacheDir.mkdirs()

            val files = listOf(
                db.templateGroupDao().getAll().toJson().toFile(cacheDir, TEMPLATE_GROUPS_FILE_NAME),
                db.templateDao().getAll().toJson().toFile(cacheDir, TEMPLATES_FILE_NAME),
                db.recipientDao().getAll().toJson().toFile(cacheDir, RECIPIENTS_FILE_NAME),
                db.recipientGroupDao().getAll().toJson().toFile(cacheDir, RECIPIENT_GROUPS_FILE_NAME)
            )
            val destFile = File(cacheDir, BACKUP_FILE_NAME)
            zipFiles(files, destFile)

            saveToDownloads(context, destFile)
            delay(600)
            _backupResult.postValue(BackupResult.SUCCESS)
        } catch (e: Exception) {
            log(e.stackTraceToString())
            _backupResult.postValue(BackupResult.FAILED)
        }

    }

    suspend fun backupDB(context: Context){
        _backupResult.postValue(BackupResult.START)
        try {
            val dbFile = context.getDatabasePath(DATABASE_NAME)
            saveToDownloads(context, dbFile)
            delay(600)
            _backupResult.postValue(BackupResult.SUCCESS)
        } catch (e: Exception) {
            log(e.stackTraceToString())
            _backupResult.postValue(BackupResult.FAILED)
        }
    }


    private fun saveToDownloads(context: Context, file: File) {
        val resolver = context.contentResolver
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, BACKUP_FILE_NAME)
                put(MediaStore.MediaColumns.MIME_TYPE, file.getMimeType())
                put(MediaStore.MediaColumns.SIZE, file.totalSpace)
            }
            resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        } else {
            val downloadsDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val destFile = File(downloadsDir, BACKUP_FILE_NAME)

            FileProvider.getUriForFile(context, FILE_PROVIDER_AUTH, destFile)
        }?.also { destUri ->
            resolver.copyFile(file.toUri(), destUri)
        }
    }

    suspend fun restoreDB(context: Context, uri: Uri) {
        AppDatabase.close()
        _restoreResult.postValue(BackupResult.START)
        var tempBackup: File? = null
        try {
            val dbFile = context.getDatabasePath(DATABASE_NAME)
            tempBackup = tempBackup(context.cacheDir, dbFile)

            val destUri = FileProvider.getUriForFile(context, FILE_PROVIDER_AUTH, dbFile)
            context.contentResolver.copyFile(uri, destUri)
            delay(600)
            _restoreResult.postValue(BackupResult.SUCCESS)
        } catch (e: Exception){
             e.printStackTrace()
            tempBackup?.let { restoreFromTempBackup(it, context.getDatabasePath(DATABASE_NAME)) }
            _restoreResult.postValue(BackupResult.FAILED)
        } finally {
            AppDatabase.init(context)
        }
    }

    private fun tempBackup(dir: File, file: File): File{
        val tempFile = File(dir, DATABASE_NAME)
        file.copyTo(tempFile)
        return tempFile
    }

    private fun restoreFromTempBackup(tempBackup: File, dbFile: File) {
        tempBackup.copyTo(dbFile)

    }
}