package com.leviancode.android.gsmbox.ui.recipients.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.RecipientGroupSelectListAdapter
import com.leviancode.android.gsmbox.databinding.DialogSelectListRecipientGroupBinding
import com.leviancode.android.gsmbox.ui.recipients.viewmodel.RecipientGroupSelectListViewModel
import com.leviancode.android.gsmbox.utils.REQUEST_SELECTED
import com.leviancode.android.gsmbox.utils.setNavigationResult

class RecipientGroupSelectListDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogSelectListRecipientGroupBinding
    private val viewModel: RecipientGroupSelectListViewModel by viewModels()
    private val args: RecipientGroupSelectListDialogArgs by navArgs()
    private lateinit var listAdapter: RecipientGroupSelectListAdapter
    private var selectedGroupName: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog is BottomSheetDialog) {
            dialog.behavior.skipCollapsed = true
            dialog.behavior.state = BottomSheetBehavior.STATE_DRAGGING
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.dialog_select_list_recipient_group, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listAdapter = RecipientGroupSelectListAdapter(viewModel)
        binding.bottomSheetRecipientGroupList.adapter = listAdapter

        viewModel.groups.observe(viewLifecycleOwner){ list ->
            list.find { it.getName() == args.groupName }?.let {
                viewModel.onItemClick(it)
            }
            listAdapter.groups = list
        }
        viewModel.selectedItem.observe(viewLifecycleOwner){
            binding.btnOkRecipientGroup.isEnabled = true
            selectedGroupName = it
        }

        binding.btnOkRecipientGroup.setOnClickListener {
            setSelectedAndExit()
        }
    }

    private fun setSelectedAndExit(){
        setNavigationResult(selectedGroupName, REQUEST_SELECTED)
        closeDialog()
    }

    private fun closeDialog() {
        findNavController().navigateUp()
    }
}