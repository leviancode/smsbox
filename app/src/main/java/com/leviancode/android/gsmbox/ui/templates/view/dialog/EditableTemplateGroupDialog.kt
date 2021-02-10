package com.leviancode.android.gsmbox.ui.templates.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.TemplateGroup
import com.leviancode.android.gsmbox.data.model.TemplateGroupObservable
import com.leviancode.android.gsmbox.databinding.DialogEditableTemplateGroupBinding
import com.leviancode.android.gsmbox.ui.templates.viewmodel.EditableTemplateGroupViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
        binding.editTextTemplateGroupName.requestFocus()

        if (args.groupId != null){
            loadGroup()
        } else {
            showKeyboard()
        }

        observeUI()
    }

    private fun loadGroup(){
        lifecycleScope.launch {
            viewModel.loadGroupById(args.groupId!!).collect { binding.toolbar.title = it }
        }
    }

    private fun observeUI(){
        binding.toolbar.setNavigationOnClickListener { closeDialog() }

        viewModel.chooseColorLiveEvent.observe(viewLifecycleOwner){ chooseColor(it) }

        viewModel.closeDialogLiveEvent.observe(viewLifecycleOwner){
            saved = true
            closeDialog()
        }
    }

    private fun chooseColor(color: Int){
        hideKeyboard()

        ColorPickerDialog(
            requireContext(),
            childFragmentManager,
            color
        ).show {
            viewModel.setIconColor(it)
        }
    }

    override fun isFieldsNotEmpty(): Boolean {
        return viewModel.isAllFieldsFilled()
    }

    private fun showMessage(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        val TAG = EditableTemplateGroupDialog::class.java.simpleName
    }
}