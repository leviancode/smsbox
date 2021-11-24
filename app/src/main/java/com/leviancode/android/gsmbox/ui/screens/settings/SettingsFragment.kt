package com.leviancode.android.gsmbox.ui.screens.settings

import android.Manifest
import android.app.DownloadManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.FragmentSettingsBinding
import com.leviancode.android.gsmbox.ui.base.BaseFragment
import com.leviancode.android.gsmbox.ui.dialogs.alertdialogs.RecoveryAlertDialog
import com.leviancode.android.gsmbox.utils.Result
import com.leviancode.android.gsmbox.utils.extensions.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(R.layout.fragment_settings) {
    private val viewModel: SettingsViewModel by viewModel()
    private lateinit var activityResultLauncher: ActivityResultLauncher<String>

    override var bottomNavViewVisibility: Int = View.VISIBLE

    override fun onCreated() {
        binding.viewModel = viewModel
        activityResultLauncher = registerForActivityResult(GetContent()) { result ->
            result?.let { restoreDB(it) }
        }
        observeEvents()
    }

    private fun observeEvents() {
        binding.apply {
            btnRestore.setOnClickListener {
                pickFile()
            }

            btnBackup.setOnClickListener {
                backupDB()
            }

            btnPlaceholders.setOnClickListener {
                openPlaceholdersFragment()
            }

            btnLanguages.setOnClickListener {
                openLanguagesFragment()
            }
        }
    }

    private fun openLanguagesFragment() {
        navigate {
            SettingsFragmentDirections.actionOpenLanguages()
        }
    }

    private fun openPlaceholdersFragment() {
        navigate {
            SettingsFragmentDirections.actionOpenPlaceholders()
        }
    }

    private fun backupDB() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            askPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) { granted ->
                if (granted) {
                    startBackup()
                } else {
                    showToast(getString(R.string.permission_denied))
                }
            }
        } else {
            startBackup()
        }
    }

    private fun startBackup() {
        viewModel.backupDatabase().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    showProgressBar()
                }
                is Result.Success -> {
                    requireView().showOpenSnackbar(getString(R.string.saved_to_downloads)) {
                        openDownloadsFolder()
                    }
                    hideProgressBar()
                }
                is Result.Failure -> {
                    showToast(getString(R.string.backup_failed))
                    hideProgressBar()
                }
            }
        }
    }

    private fun restoreDB(uri: Uri) {
        if (uri.isValidSQLite(requireContext())) {
            RecoveryAlertDialog(requireContext()).show { response ->
                if (response) {
                    restoreDatabase(uri)
                }
            }
        } else {
            showToast(getString(R.string.invalid_file))
        }
    }

    private fun restoreDatabase(uri: Uri) {
        viewModel.restoreDB(uri).observe(viewLifecycleOwner){ result ->
            when (result) {
                is Result.Loading -> {
                    showProgressBar()
                }
                is Result.Success -> {
                    showToast(getString(R.string.restore_success))
                    hideProgressBar()
                }
                is Result.Failure -> {
                    showToast(getString(R.string.restore_failed))
                    hideProgressBar()
                }
            }
        }
    }


    private fun pickFile() {
        activityResultLauncher.launch("application/octet-stream")
    }

    private fun hideProgressBar() {
        binding.apply {
            progressBar.visibility = View.INVISIBLE
            btnBackup.isEnabled = true
            btnRestore.isEnabled = true
        }
    }

    private fun showProgressBar() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            btnBackup.isEnabled = false
            btnRestore.isEnabled = false
        }
    }

    private fun openDownloadsFolder() {
        startActivity(Intent(DownloadManager.ACTION_VIEW_DOWNLOADS))
    }
}