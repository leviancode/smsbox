package com.leviancode.android.gsmbox.ui.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.DialogNewGroupBinding
import com.leviancode.android.gsmbox.ui.viewmodel.TemplateGroupObservable
import com.leviancode.android.gsmbox.ui.viewmodel.TemplatesViewModel

class NewGroupDialog() : DefaultFullScreenDialog() {
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
        binding.viewModel = groupObservable
        binding.toolbar.apply {
            title = getString(R.string.new_group)
            setNavigationOnClickListener { v: View? ->
                showDiscardDialog()
            }
            setOnMenuItemClickListener { item ->
                viewModel.addGroup(groupObservable.group)
                dismiss()
                true
            }
        }
    }

    private fun showDiscardDialog(){
        dismiss()
    }

    fun show(manager: FragmentManager) {
        super.show(manager, TAG)
    }

    companion object {
        val TAG = NewGroupDialog::class.java.simpleName
    }
}