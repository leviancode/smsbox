package com.leviancode.android.gsmbox.ui.screens.templates.groups.edit

import android.view.View
import androidx.navigation.fragment.navArgs
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.utils.hideKeyboard
import com.leviancode.android.gsmbox.utils.showKeyboard
import com.leviancode.android.gsmbox.databinding.FragmentTemplateGroupEditBinding
import com.leviancode.android.gsmbox.ui.base.BaseFragment
import com.leviancode.android.gsmbox.ui.dialogs.ColorPickerDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class TemplateGroupEditFragment : BaseFragment<FragmentTemplateGroupEditBinding>(R.layout.fragment_template_group_edit) {
    private val viewModel: TemplateGroupEditViewModel by viewModel()
    private val args: TemplateGroupEditFragmentArgs by navArgs()

    override var bottomNavViewVisibility: Int = View.GONE

    override val showConfirmationDialogBeforeQuit: Boolean
        get() = viewModel.isDataSavedOrNotChanged()

    override fun onCreated() {
        binding.viewModel = viewModel
        setTitle(args.groupId != 0)
        showKeyboard()
        loadData()
        observeEvents()
    }

    private fun loadData() {
        viewModel.loadGroup(args.groupId).observe(viewLifecycleOwner){ group ->

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