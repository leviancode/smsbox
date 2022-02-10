package com.brainymobile.android.smsbox.ui.screens.templates.groups.edit

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.brainymobile.android.smsbox.R
import com.brainymobile.android.smsbox.databinding.FragmentTemplateGroupEditBinding
import com.brainymobile.android.smsbox.ui.base.BaseFragment
import com.brainymobile.android.smsbox.ui.dialogs.ColorPickerDialog
import com.brainymobile.android.smsbox.utils.extensions.observe
import com.brainymobile.android.smsbox.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TemplateGroupEditFragment :
    BaseFragment<FragmentTemplateGroupEditBinding>(R.layout.fragment_template_group_edit) {
    private val viewModel: TemplateGroupEditViewModel by viewModels()
    private val args: TemplateGroupEditFragmentArgs by navArgs()

    override var bottomNavViewVisibility: Int = View.GONE

    override val showConfirmationDialogBeforeQuit: Boolean
        get() = !viewModel.isDataSavedOrNotChanged()

    override val showKeyboardOnStarted: Boolean = true

    override fun onCreated() {
        binding.viewModel = viewModel
        setTitle(args.groupId != 0)
        loadData()
        observeEvents()
    }

    private fun loadData() {
        viewModel.loadGroup(args.groupId).observe(viewLifecycleOwner) { group ->
            binding.model = group
            binding.executePendingBindings()
        }
    }

    private fun setTitle(editMode: Boolean) {
        if (editMode) binding.toolbar.title = getString(R.string.edit_group)
    }

    private fun observeEvents() {
        binding.toolbar.setNavigationOnClickListener { close() }

        viewModel.chooseColorEvent.observe(viewLifecycleOwner) { selectColor(it) }

        viewModel.quitEvent.observe(viewLifecycleOwner) { close() }
    }

    private fun selectColor(color: String) {
        hideKeyboard()

        ColorPickerDialog(
            requireContext(),
            childFragmentManager,
            color
        ).show {
            viewModel.setIconColor(it)
        }
    }
}