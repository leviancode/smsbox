package com.leviancode.android.gsmbox.ui.fragments.settings

import android.Manifest
import android.app.DownloadManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.FragmentSettingsBinding
import com.leviancode.android.gsmbox.core.utils.helpers.BackupResult.*
import com.leviancode.android.gsmbox.ui.dialogs.alertdialogs.RecoveryAlertDialog
import com.leviancode.android.gsmbox.core.utils.*
import com.leviancode.android.gsmbox.core.utils.extensions.askPermission
import com.leviancode.android.gsmbox.core.utils.extensions.isValidSQLite
import com.leviancode.android.gsmbox.core.utils.extensions.navigate
import com.leviancode.android.gsmbox.core.utils.extensions.showToast

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var activityResultLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        activityResultLauncher = registerForActivityResult(GetContent()) { result ->
            result?.let { restoreDB(it) }
        }
        observeEvents()
    }

    private fun restoreDB(uri: Uri) {
        if (uri.isValidSQLite(requireContext())){
            RecoveryAlertDialog(requireContext()).show { response ->
                if (response) {
                    viewModel.restoreDB(requireContext(), uri)
                }
            }
        } else {
            showToast(getString(R.string.invalid_file))
        }
    }

    private fun observeEvents() {
            viewModel.backupEvent.observe(viewLifecycleOwner) { result ->
                when(result) {
                    START -> showProgressBar()
                    SUCCESS -> {
                        showOpenSnackbar(requireView(), getString(R.string.saved_to_downloads)) {
                            openDownloadsFolder()
                        }
                        hideProgressBar()
                    }
                    FAILED -> {
                        showToast( getString(R.string.backup_failed))
                        hideProgressBar()
                    }
                    else -> {}
                }
            }

            viewModel.restoreEvent.observe(viewLifecycleOwner) { result ->
                when(result) {
                    START -> showProgressBar()
                    SUCCESS -> {
                        showToast(getString(R.string.restore_success))
                        hideProgressBar()

                    }
                    FAILED -> {
                        showToast( getString(R.string.restore_failed))
                        hideProgressBar()
                    }
                    else -> {}
                }
            }

        binding.btnRestore.setOnClickListener {
            pickFile()
        }

        binding.btnBackup.setOnClickListener {
            backupDB()
        }

        binding.btnPlaceholders.setOnClickListener {
            openPlaceholdersFragment()
        }

        binding.btnLanguages.setOnClickListener {
            openLanguagesFragment()
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
            askPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE){ result ->
                if (result){
                    viewModel.backupDB(requireContext())
                } else {
                    showToast(getString(R.string.permission_denied))
                }
            }
        } else {
            viewModel.backupDB(requireContext())
        }
    }


    private fun pickFile() {
        activityResultLauncher.launch("application/octet-stream")
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnBackup.isEnabled = true
        binding.btnRestore.isEnabled = true
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnBackup.isEnabled = false
        binding.btnRestore.isEnabled = false
    }

    private fun openDownloadsFolder() {
        startActivity(Intent(DownloadManager.ACTION_VIEW_DOWNLOADS))
    }
}