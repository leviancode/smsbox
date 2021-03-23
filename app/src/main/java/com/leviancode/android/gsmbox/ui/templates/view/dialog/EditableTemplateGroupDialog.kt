package com.leviancode.android.gsmbox.ui.templates.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.DialogEditableTemplateGroupBinding
import com.leviancode.android.gsmbox.ui.templates.viewmodel.EditableTemplateGroupViewModel
import com.leviancode.android.gsmbox.utils.RESULT_CANCEL
import com.leviancode.android.gsmbox.utils.RESULT_OK
import com.leviancode.android.gsmbox.utils.hideKeyboard
import com.leviancode.android.gsmbox.utils.showKeyboard

class EditableTemplateGroupDialog : AbstractFullScreenDialog() {
    private lateinit var binding: DialogEditableTemplateGroupBinding
    private val viewModel: EditableTemplateGroupViewModel by viewModels()
    private val args: EditableTemplateGroupDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.dialog_editable_template_group, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        viewModel.setGroup(args.group)
        showKeyboard(binding.editTextTemplateGroupName)
        args.group.getName().takeIf { it.isNotBlank() }?.let {
            binding.toolbar.title = it
        }

        observeUI()
    }

    private fun observeUI(){
        binding.toolbar.setNavigationOnClickListener { closeDialog(RESULT_CANCEL) }

        viewModel.chooseColorLiveEvent.observe(viewLifecycleOwner){ selectColor(it) }

        viewModel.savedLiveEvent.observe(viewLifecycleOwner){ closeDialog(RESULT_OK) }
    }

    private fun selectColor(color: Int){
        hideKeyboard()

        ColorPickerDialog(
            requireContext(),
            childFragmentManager,
            color
        ).show {
            viewModel.setIconColor(it)
        }
    }

    override fun isDataEdited(): Boolean {
        return viewModel.isGroupEdited()
    }
}