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
import com.leviancode.android.gsmbox.adapters.RecipientSelectListAdapter
import com.leviancode.android.gsmbox.data.model.Recipient
import com.leviancode.android.gsmbox.databinding.DialogSelectListRecipientBinding
import com.leviancode.android.gsmbox.ui.recipients.viewmodel.RecipientSelectListViewModel
import com.leviancode.android.gsmbox.utils.REQUEST_SELECTED
import com.leviancode.android.gsmbox.utils.setNavigationResult

class RecipientSelectListDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogSelectListRecipientBinding
    private val viewModel: RecipientSelectListViewModel by viewModels()
    private val args: RecipientSelectListDialogArgs by navArgs()
    private lateinit var listAdapter: RecipientSelectListAdapter
    private var selectedRecipient: Recipient? = null

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
            inflater, R.layout.dialog_select_list_recipient, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listAdapter = RecipientSelectListAdapter(viewModel)
        binding.bottomSheetRecipientList.adapter = listAdapter

        viewModel.recipients.observe(viewLifecycleOwner){ list ->
            list.find { it.getRecipientId() == args.recipientId }?.let {
                viewModel.onItemClick(it)
            }
            if (list.isEmpty()){
                binding.tvNoRecipients.visibility = View.VISIBLE
                binding.btnOkRecipient.isEnabled = true
            } else {
                binding.tvNoRecipients.visibility = View.GONE
            }

            listAdapter.recipients = list
        }
        viewModel.selectedItem.observe(viewLifecycleOwner){
            binding.btnOkRecipient.isEnabled = true
            selectedRecipient = it
        }

        binding.btnOkRecipient.setOnClickListener {
            setSelectedAndExit()
        }
    }

    private fun setSelectedAndExit(){
        setNavigationResult(selectedRecipient, REQUEST_SELECTED)
        closeDialog()
    }

    private fun closeDialog() {
        findNavController().navigateUp()
    }
}