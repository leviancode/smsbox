package com.leviancode.android.gsmbox.ui.templates.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.DialogNewGroupBinding
import com.leviancode.android.gsmbox.data.model.TemplateGroupObservable
import com.leviancode.android.gsmbox.ui.templates.viewmodel.TemplateGroupViewModel

class NewGroupDialog : AbstractFullScreenDialog() {
    private lateinit var binding: DialogNewGroupBinding
    private val viewModel: TemplateGroupViewModel by viewModels()
    override var saved = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.dialog_new_group, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.toolbar.apply {
            setNavigationOnClickListener { v: View? ->
                closeDialog()
            }
            setOnMenuItemClickListener { item ->
                saveGroup()
                closeDialog()
                true
            }
        }

        binding.editTextTemplateGroupName.requestFocus()
        observeUI()
    }

    private fun observeUI(){
        viewModel.chooseColorLiveEvent.observe(viewLifecycleOwner){ chooseColor(it) }

        val btn = binding.toolbar.menu.findItem(R.id.menu_save)
        viewModel.fieldsNotEmptyLiveEvent.observe(viewLifecycleOwner) { btn.isEnabled = it }
    }

    private fun saveGroup(){
        saved = true
        viewModel.saveGroup()
    }

    private fun chooseColor(group: TemplateGroupObservable){
        hideKeyboard()

        ColorPickerDialog(
            requireContext(),
            childFragmentManager,
            group.getTemplateGroupIconColor()
        ).show {
            group.setTemplateGroupIconColor(it)
        }
    }

    override fun isFieldsNotEmpty(): Boolean {
        return viewModel.isFieldsNotEmpty()
    }

    private fun showMessage(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        val TAG = NewGroupDialog::class.java.simpleName
    }
}