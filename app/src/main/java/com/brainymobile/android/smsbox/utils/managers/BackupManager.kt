package com.brainymobile.android.smsbox.utils.managers

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.brainymobile.android.smsbox.data.database.AppDatabase
import com.brainymobile.android.smsbox.utils.*
import com.brainymobile.android.smsbox.utils.extensions.*
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class BackupManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val filesManager: FilesManager,
    private val database: AppDatabase
) {

    fun backupToJson() = flow {
        emit(Result.Loading)
        try {
            val cacheDir = filesManager.createCacheDir(BACKUP_DIR)

            val files = listOf(
                database.templateGroupDao().getAll().toJson().toFile(cacheDir, TEMPLATE_GROUPS_FILE_NAME),
                database.templateDao().getAll().toJson().toFile(cacheDir, TEMPLATES_FILE_NAME),
                database.recipientDao().getAll().toJson().toFile(cacheDir, RECIPIENTS_FILE_NAME),
                database.recipientGroupDao().getAll().toJson().toFile(cacheDir, RECIPIENT_GROUPS_FILE_NAME)
            )
            val destFile = File(cacheDir, BACKUP_FILE_NAME)
            files.zipFiles(destFile)

            val uri = filesManager.saveToDownloads(destFile, BACKUP_FILE_NAME)
            delay(600)
            emit(Result.Success(uri))
        } catch (e: Exception) {
            logE<BackupManager>(e.message ?: "Backup failed")
            emit(Result.Failure(e.message ?: "Backup failed"))
        }
    }

    fun backupDB() = flow {
        emit(Result.Loading)
        try {
            val dbFile = context.getDatabasePath(DATABASE_NAME)
            val uri = filesManager.saveToDownloads(dbFile, BACKUP_FILE_NAME)
            delay(600)
            emit(Result.Success(uri))
        } catch (e: Exception) {
            logE<BackupManager>(e.message ?: "Backup failed")
            emit(Result.Failure(e.message ?: ""))
        }
    }

    fun restoreDB(uri: Uri) = flow {
        AppDatabase.close()
        emit(Result.Loading)
        var tempBackup: File? = null
        try {
            val dbFile = context.getDatabasePath(DATABASE_NAME)
            tempBackup = tempBackup(context.cacheDir, dbFile)

            val destUri = FileProvider.getUriForFile(context, FILE_PROVIDER_AUTH, dbFile)
            context.contentResolver.copyFile(uri, destUri)
            delay(600)
            emit(Result.Success())
        } catch (e: Exception){
            logE<BackupManager>(e.message ?: "Backup failed")
            tempBackup?.let { restoreFromTempBackup(it, context.getDatabasePath(DATABASE_NAME)) }
            emit(Result.Failure(e.message ?: ""))
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

    companion object{
        val BACKUP_FILE_NAME
            get() = "smsbox_backup_${getFormatDate()}.sqlite"
        val BACKUP_DIR
            get() = "backup_${getFormatDate()}"
    }
}