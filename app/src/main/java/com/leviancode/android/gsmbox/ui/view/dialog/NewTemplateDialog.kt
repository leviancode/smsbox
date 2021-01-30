package com.leviancode.android.gsmbox.ui.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.DialogNewTemplateBinding
import com.leviancode.android.gsmbox.ui.viewmodel.TemplateObservable
import com.leviancode.android.gsmbox.ui.viewmodel.TemplatesViewModel


class NewTemplateDialog : AbstractFullScreenDialog(){
    private lateinit var binding: DialogNewTemplateBinding
    private val templateObservable = TemplateObservable()
    private val viewModel: TemplatesViewModel by activityViewModels()
    private val args: NewTemplateDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.dialog_new_template, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        templateObservable.setGroupId(args.groupId)
        binding.template = templateObservable
        binding.editTextTemplateName.requestFocus()

        binding.toolbar.apply {
            title = getString(R.string.title_new_template)
            setNavigationOnClickListener { v: View? ->
                showDiscardDialog()
            }
            setOnMenuItemClickListener { item ->
                viewModel.addTemplate(templateObservable.template)
                dismiss()
                true
            }
        }


        val btn = binding.toolbar.menu.findItem(R.id.menu_save)
        binding.editTextTemplateName.doOnTextChanged { text, start, before, count ->
            btn.isEnabled = count > 0
        }

        binding.btnTemplateIconColor.setOnClickListener { chooseColor() }
    }

    private fun chooseColor(){
        hideKeyboard()

        ColorPickerBottomSheet(
            requireContext(),
            childFragmentManager,
            templateObservable.getIconColor()
        ).show {
            templateObservable.setIconColor(it)
        }
    }

    private fun showDiscardDialog(){
        hideKeyboard()
        if (templateObservable.isFieldsEmpty()){
            dismiss()
        } else {
            DiscardDialog(requireContext()).show{ response ->
                if (response) dismiss()
            }
        }
    }

    companion object {
        private val TAG = NewTemplateDialog::class.java.simpleName
    }
}