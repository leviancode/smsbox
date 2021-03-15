package com.leviancode.android.gsmbox.ui.settings

import android.Manifest
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.BasePermissionListener
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.repository.FilesRepository
import com.leviancode.android.gsmbox.helpers.BackupResult
import com.leviancode.android.gsmbox.utils.SingleLiveEvent
import com.leviancode.android.gsmbox.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
   // val backupEvent = SingleLiveEvent<BackupResult>()
      val backupEvent = FilesRepository.result


    fun onSaveClick(view: View){
        val context = view.context
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
            Dexter.withContext(context)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : BasePermissionListener(){
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        backup(view)
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        showToast(context, context.getString(R.string.permission_denied))
                    }
                }).check()
        } else {
            backup(view)
        }
    }

    private fun backup(view: View){
        viewModelScope.launch(Dispatchers.IO) {
            FilesRepository.backup(view.context)
        }
    }

    fun onRestoreClick(v: View){

    }
}