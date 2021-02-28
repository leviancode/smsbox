package com.leviancode.android.gsmbox.ui.recipients.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.coordinatorlayout.widget.CoordinatorLayout
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
import com.leviancode.android.gsmbox.utils.ContactsManager
import com.leviancode.android.gsmbox.utils.REQUEST_SELECTED
import com.leviancode.android.gsmbox.utils.REQUEST_SELECT_RECIPIENT
import com.leviancode.android.gsmbox.utils.setNavigationResult

class RecipientSelectListDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogSelectListRecipientBinding
    private val viewModel: RecipientSelectListViewModel by viewModels()
    private val args: RecipientSelectListDialogArgs by navArgs()
    private lateinit var listAdapter: RecipientSelectListAdapter
    private lateinit var contactsLauncher: ActivityResultLauncher<Void>
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
        contactsLauncher = registerForActivityResult(ActivityResultContracts.PickContact()) { result ->
            selectedRecipient = ContactsManager.parseUri(requireContext(), result)
            setSelectedAndExit()
        }
        listAdapter = RecipientSelectListAdapter(viewModel)
        binding.bottomSheetRecipientList.adapter = listAdapter

        /*val layoutParams = binding.bottomSheet.layoutParams as CoordinatorLayout.LayoutParams
        val bottomSheetBehavior = layoutParams.behavior as BottomSheetBehavior
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {}
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.bottomSheet.progress = slideOffset
            }
        })*/



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

        binding.btnContacts.setOnClickListener {
            selectContact()
        }
    }

    private fun selectContact() {
        ContactsManager.openContactsApp(requireContext(), contactsLauncher)
    }

    private fun setSelectedAndExit(){
        setNavigationResult(selectedRecipient, REQUEST_SELECT_RECIPIENT)
        closeDialog()
    }

    private fun closeDialog() {
        findNavController().navigateUp()
    }
}