package com.leviancode.android.gsmbox.ui.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.DialogNewTemplateBinding
import com.leviancode.android.gsmbox.ui.viewmodel.TemplateGroupViewModel
import com.leviancode.android.gsmbox.ui.viewmodel.TemplateViewModel


class NewTemplateDialog : DialogFragment() {
    private lateinit var binding: DialogNewTemplateBinding
    private val groupViewModel = TemplateGroupViewModel()
    private val templateViewModel = TemplateViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

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
        binding.group = groupViewModel
        binding.template = templateViewModel

        binding.toolbar.apply {
            inflateMenu(R.menu.menu_new_template)
            title = getString(R.string.title_new_template)
            setNavigationOnClickListener { v: View? ->
                showDiscardDialog()
            }
            setOnMenuItemClickListener { item ->
                groupViewModel.saveTemplate(templateViewModel.template)
                dismiss()
                true
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.apply {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            window?.setLayout(width, height)
            window?.setWindowAnimations(R.style.Theme_GsmBox_Slide)
        }
    }

    private fun showDiscardDialog(){
        dismiss()
    }

    companion object {
        val TAG = NewTemplateDialog::class.java.simpleName

        fun display(fragmentManager: FragmentManager): NewTemplateDialog {
            val dialog = NewTemplateDialog()
            dialog.show(fragmentManager, TAG)
            return dialog
        }
    }
}