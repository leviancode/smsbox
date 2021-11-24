package com.leviancode.android.gsmbox.ui.screens.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.leviancode.android.gsmbox.utils.managers.BackupManager

class SettingsViewModel(
    private val backupManager: BackupManager
) : ViewModel() {

    fun backupDatabase() = backupManager.backupDB().asLiveData()

    fun restoreDB(uri: Uri) =  backupManager.restoreDB(uri).asLiveData()

}