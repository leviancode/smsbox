package com.leviancode.android.gsmbox.ui.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.DialogNewTemplateBinding
import com.leviancode.android.gsmbox.ui.viewmodel.TemplateObservable
import com.leviancode.android.gsmbox.ui.viewmodel.TemplatesViewModel
import com.leviancode.android.gsmbox.utils.ARG_GROUP_ID


class NewTemplateDialog : DefaultFullScreenDialog(){
    private lateinit var binding: DialogNewTemplateBinding
    private val templateObservable = TemplateObservable()
    private val viewModel: TemplatesViewModel by activityViewModels()

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
        binding.template = templateObservable

        arguments?.getString(ARG_GROUP_ID)?.let { templateObservable.setGroupId(it) }

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

        binding.btnTemplateIconColor.setOnClickListener {

        }

    }

    private fun showDiscardDialog(){
        dismiss()
    }

    companion object {
        private val TAG = NewTemplateDialog::class.java.simpleName

        fun display(manager: FragmentManager, groupId: String): NewTemplateDialog{
            val args = Bundle()
            args.putString(ARG_GROUP_ID, groupId)
            val dialog = NewTemplateDialog().apply {
                this.arguments = args
            }
            dialog.show(manager, TAG)
            return dialog
        }
    }
}