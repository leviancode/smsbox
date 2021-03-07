package com.leviancode.android.gsmbox.ui.recipients.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.RecipientSelectListAdapter
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.databinding.ContactsButtonBinding
import com.leviancode.android.gsmbox.databinding.DialogSelectListRecipientBinding
import com.leviancode.android.gsmbox.ui.recipients.viewmodel.RecipientSelectListViewModel
import com.leviancode.android.gsmbox.utils.ContactsManager
import com.leviancode.android.gsmbox.utils.REQUEST_SELECT_RECIPIENT
import com.leviancode.android.gsmbox.utils.goBack
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
            dialog.setOnShowListener {
                val container = dialog.findViewById<FrameLayout>(R.id.container)
                val bind = ContactsButtonBinding.inflate(dialog.layoutInflater)
                container?.addView(bind.root)
                bind.btnContacts.setOnClickListener {
                    selectContact()
                }
            }
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
        contactsLauncher =
            registerForActivityResult(ActivityResultContracts.PickContact()) { result ->
                result?.let {
                    selectedRecipient = ContactsManager.parseUri(requireContext(), result)
                    setSelectedAndExit()
                }
            }

        listAdapter = RecipientSelectListAdapter(viewModel)
        binding.bottomSheetRecipientList.adapter = listAdapter

        observeUI()
    }

    private fun observeUI() {
        viewModel.recipients.observe(viewLifecycleOwner) { list ->
            list.find { it.getRecipientId() == args.recipientId }?.let {
                viewModel.onItemClick(it)
            }
            if (list.isEmpty()) {
                binding.tvNoRecipients.visibility = View.VISIBLE
                binding.btnOkRecipient.isEnabled = true
            } else {
                binding.tvNoRecipients.visibility = View.GONE
            }

            listAdapter.recipients = list
        }
        viewModel.selectedItem.observe(viewLifecycleOwner) {
            binding.btnOkRecipient.isEnabled = true
            selectedRecipient = it
        }

        binding.btnOkRecipient.setOnClickListener {
            setSelectedAndExit()
        }
    }

    private fun selectContact() {
        ContactsManager.openContactsApp(requireContext(), contactsLauncher)
    }

    private fun setSelectedAndExit() {
        setNavigationResult(selectedRecipient, REQUEST_SELECT_RECIPIENT)
        goBack()
    }
}