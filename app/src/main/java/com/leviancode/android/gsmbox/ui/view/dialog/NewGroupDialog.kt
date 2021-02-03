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
import com.leviancode.android.gsmbox.data.model.TemplateGroupObservable
import com.leviancode.android.gsmbox.ui.viewmodel.TemplateGroupListViewModel

class NewGroupDialog : AbstractFullScreenDialog() {
    private lateinit var binding: DialogNewGroupBinding
    private val groupObservable = TemplateGroupObservable()
    private val viewModel: TemplateGroupListViewModel by activityViewModels()
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
        binding.group = groupObservable
        binding.toolbar.apply {
            title = getString(R.string.new_group)
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
        val btn = binding.toolbar.menu.findItem(R.id.menu_save)
        binding.editTextTemplateGroupName.doOnTextChanged { text, start, before, count ->
            btn.isEnabled = count > 0
        }

        binding.btnTemplateGroupIconColor.setOnClickListener{ chooseColor() }
    }

    private fun saveGroup(){
        saved = true
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

    override fun isFieldsEmpty(): Boolean {
        return groupObservable.isFieldsEmpty()
    }

    companion object {
        val TAG = NewGroupDialog::class.java.simpleName
    }
}