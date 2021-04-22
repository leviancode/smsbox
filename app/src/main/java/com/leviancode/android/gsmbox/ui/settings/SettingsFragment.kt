package com.leviancode.android.gsmbox.ui.settings

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
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.BasePermissionListener
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.FragmentSettingsBinding
import com.leviancode.android.gsmbox.utils.helpers.BackupResult.*
import com.leviancode.android.gsmbox.ui.extra.alertdialogs.RecoveryAlertDialog
import com.leviancode.android.gsmbox.utils.*
import com.leviancode.android.gsmbox.utils.extensions.isValidSQLite
import com.leviancode.android.gsmbox.utils.extensions.navigate

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
        observeUI()
    }

    private fun restoreDB(uri: Uri) {
        if (uri.isValidSQLite(requireContext())){
            RecoveryAlertDialog(requireContext()).show { response ->
                if (response) {
                    viewModel.restoreDB(requireContext(), uri)
                }
            }
        } else {
            showToast(requireContext(), getString(R.string.invalid_file))
        }
    }

    private fun observeUI() {
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
                        showToast(requireContext(), getString(R.string.backup_failed))
                        hideProgressBar()
                    }
                    else -> {}
                }
            }

            viewModel.restoreEvent.observe(viewLifecycleOwner) { result ->
                when(result) {
                    START -> showProgressBar()
                    SUCCESS -> {
                        showToast(requireContext(), getString(R.string.restore_success))
                        hideProgressBar()

                    }
                    FAILED -> {
                        showToast(requireContext(), getString(R.string.restore_failed))
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
    }

    private fun openPlaceholdersFragment() {
        navigate {
            SettingsFragmentDirections.actionOpenPlaceholders()
        }
    }

    private fun backupDB() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Dexter.withContext(context)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : BasePermissionListener() {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        viewModel.backupDB(requireContext())
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        showToast(requireContext(), getString(R.string.permission_denied))
                    }
                }).check()
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