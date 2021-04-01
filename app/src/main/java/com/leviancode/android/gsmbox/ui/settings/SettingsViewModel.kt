package com.leviancode.android.gsmbox.ui.settings

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leviancode.android.gsmbox.utils.managers.BackupManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    val backupEvent = BackupManager.backupResult
    val restoreEvent = BackupManager.restoreResult

    fun backupDB(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            BackupManager.backupDB(context)
        }
    }

    fun restoreDB(context: Context, uri: Uri){
        viewModelScope.launch(Dispatchers.IO) {
            BackupManager.restoreDB(context, uri)
        }
    }

}