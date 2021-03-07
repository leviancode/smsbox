package com.leviancode.android.gsmbox.ui.recipients.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.DialogEditableRecipientBinding
import com.leviancode.android.gsmbox.ui.recipients.viewmodel.EditableRecipientViewModel
import com.leviancode.android.gsmbox.ui.templates.view.dialog.AbstractFullScreenDialog
import com.leviancode.android.gsmbox.utils.*

class EditableRecipientDialog : AbstractFullScreenDialog() {
    private lateinit var binding: DialogEditableRecipientBinding
    private val viewModel: EditableRecipientViewModel by viewModels()
    private val args: EditableRecipientDialogArgs by navArgs()
    private lateinit var contactsLauncher: ActivityResultLauncher<Void>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_editable_recipient, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        args.recipient.let {
            viewModel.setRecipient(it)
            if (it.recipientName.isNotBlank()) {
                binding.toolbar.title = it.recipientName
            } else if (it.recipientName.isBlank() && it.phoneNumber.isNotBlank()) {
                binding.btnRecipientContacts.visibility = View.GONE
            }
        }
        setupContactPickerLauncher()
        showKeyboard(binding.editTextRecipientName)
        observeUI()
    }

    private fun setupContactPickerLauncher(){
        contactsLauncher = registerForActivityResult(PickContact()) { result ->
            ContactsManager.parseUri(requireContext(), result)?.let {
                viewModel.setRecipient(
                    args.recipient.apply {
                        recipientName = it.recipientName
                        phoneNumber = it.phoneNumber
                    }
                )
            }
        }
    }

    private fun observeUI(){
        binding.toolbar.setNavigationOnClickListener { closeDialog(RESULT_CANCEL) }

        viewModel.savedLiveEvent.observe(viewLifecycleOwner){ closeDialog(RESULT_OK) }

        viewModel.selectContactLiveEvent.observe(viewLifecycleOwner){ selectContact() }

        viewModel.selectGroupLiveEvent.observe(viewLifecycleOwner){ selectGroup(it) }

        viewModel.groups.observe(viewLifecycleOwner){ list ->
            updateAutoCompleteList(list.map { it.groupName })
        }
    }

    private fun updateAutoCompleteList(list: List<String>){
        binding.autoCompleteList = list
        binding.executePendingBindings()
    }

    private fun selectGroup(groupName: String?) {
        hideKeyboard()
        getNavigationResult<String>(REQUEST_SELECTED)?.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                viewModel.setGroupName(result)
                removeNavigationResult<String>(REQUEST_SELECTED)
            }
        }
        navigate {
            EditableRecipientDialogDirections.actionOpenRecipientGroupList(groupName)
        }
    }

    private fun selectContact() {
        hideKeyboard()
        ContactsManager.openContactsApp(requireContext(), contactsLauncher)
    }

    override fun isDataEdited() = viewModel.isRecipientEdited()

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }
}