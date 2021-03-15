package com.leviancode.android.gsmbox.ui.settings

import android.app.DownloadManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.FragmentSettingsBinding
import com.leviancode.android.gsmbox.helpers.BackupResult
import com.leviancode.android.gsmbox.utils.FILE_PROVIDER_AUTH
import com.leviancode.android.gsmbox.utils.PICK_ZIP_FILE
import com.leviancode.android.gsmbox.utils.showOpenSnackbar
import com.leviancode.android.gsmbox.utils.showToast
import kotlinx.coroutines.flow.collect

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var activityResultLauncher: ActivityResultLauncher<Uri>

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
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { result ->

        }
        observeUI()
    }

    private fun observeUI() {
        lifecycleScope.launchWhenStarted {
            viewModel.backupEvent.collect { result ->
                when(result) {
                    BackupResult.START -> {
                        showProgressBar()
                    }
                    BackupResult.SUCCESS -> {
                        showOpenSnackbar(requireView(), getString(R.string.saved_to_downloads)){
                            openDownloadsFolder()
                        }
                        hideProgressBar()
                    }
                    BackupResult.FAILED -> {
                        showToast(requireContext(), getString(R.string.backup_failed))
                        hideProgressBar()
                    }
                    else -> {}
                }
            }
        }

        binding.btnRestore.setOnClickListener {
            pickFile()
        }

    }


    private fun pickFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"

            // Optionally, specify a URI for the file that should appear in the
            // system file picker when it loads.
           // putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
        }

      //  activityResultLauncher.launch("application/zip")
        requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.let { file ->
            val uri = FileProvider.getUriForFile(requireContext(), FILE_PROVIDER_AUTH, file)
            activityResultLauncher.launch(uri)
        }
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