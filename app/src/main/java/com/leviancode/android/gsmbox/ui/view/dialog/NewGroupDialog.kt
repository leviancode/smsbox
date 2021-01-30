package com.leviancode.android.gsmbox.ui.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.DialogNewGroupBinding
import com.leviancode.android.gsmbox.ui.viewmodel.TemplateGroupObservable
import com.leviancode.android.gsmbox.ui.viewmodel.TemplatesViewModel

class NewGroupDialog : AbstractFullScreenDialog() {
    private lateinit var binding: DialogNewGroupBinding
    private val groupObservable = TemplateGroupObservable()
    private val viewModel: TemplatesViewModel by activityViewModels()

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
        binding.group = groupObservable
        binding.toolbar.apply {
            title = getString(R.string.new_group)
            setNavigationOnClickListener { v: View? ->
                showDiscardDialog()
            }
            setOnMenuItemClickListener { item ->
                saveGroup()
                dismiss()
                true
            }
        }

        binding.editTextGroupName.requestFocus()
        val btn = binding.toolbar.menu.findItem(R.id.menu_save)
        binding.editTextGroupName.doOnTextChanged { text, start, before, count ->
                btn.isEnabled = count > 0
        }

        binding.btnTemplateIconColor.setOnClickListener{ chooseColor() }
    }

    private fun saveGroup(){
        viewModel.addGroup(groupObservable.group)
    }

    private fun chooseColor(){
        hideKeyboard()

        ColorPickerBottomSheet(
            requireContext(),
            childFragmentManager,
            groupObservable.getIconColor()
        ).show {
            groupObservable.setIconColor(it)
        }
    }

    private fun showDiscardDialog(){
        hideKeyboard()
        if (groupObservable.isFieldsEmpty()){
            dismiss()
        } else {
            DiscardDialog(requireContext()).show{ response ->
                if (response) dismiss()
            }
        }
    }

    companion object {
        val TAG = NewGroupDialog::class.java.simpleName
    }
}