package com.brainymobile.android.smsbox.ui.screens.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.brainymobile.android.smsbox.utils.managers.BackupManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val backupManager: BackupManager
) : ViewModel() {

    fun backupDatabase() = backupManager.backupDB().asLiveData()

    fun restoreDB(uri: Uri) =  backupManager.restoreDB(uri).asLiveData()

}