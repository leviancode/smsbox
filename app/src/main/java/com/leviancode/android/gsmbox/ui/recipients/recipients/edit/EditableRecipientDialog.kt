package com.leviancode.android.gsmbox.ui.recipients.recipients.edit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.PickContact
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.databinding.DialogEditableRecipientBinding
import com.leviancode.android.gsmbox.databinding.ViewRecipientGroupHolderBinding
import com.leviancode.android.gsmbox.ui.extra.AbstractFullScreenEditableDialog
import com.leviancode.android.gsmbox.utils.*
import com.leviancode.android.gsmbox.utils.extensions.getNavigationResult
import com.leviancode.android.gsmbox.utils.extensions.navigate
import com.leviancode.android.gsmbox.utils.extensions.removeNavigationResult
import com.leviancode.android.gsmbox.utils.managers.ContactsManager

class EditableRecipientDialog :
    AbstractFullScreenEditableDialog<DialogEditableRecipientBinding>(R.layout.dialog_editable_recipient) {
    private lateinit var binding: DialogEditableRecipientBinding
    private val viewModel: EditableRecipientViewModel by viewModels()
    private val args: EditableRecipientDialogArgs by navArgs()
    private lateinit var contactsLauncher: ActivityResultLauncher<Void>

    override fun setupViews(binding: DialogEditableRecipientBinding) {
        this.binding = binding
        binding.viewModel = viewModel
        setTitle(args.recipientId != 0)
        setupContactPickerLauncher()
        showKeyboard(binding.editTextRecipientName)
        fetchData()
        observeEvents()
    }

    private fun setTitle(editMode: Boolean) {
        if (editMode) binding.toolbar.title = getString(R.string.edit_recipient)
    }

    private fun setupContactPickerLauncher() {
        contactsLauncher = registerForActivityResult(PickContact()) { result ->
            ContactsManager.parseUri(requireContext(), result)?.let {
                viewModel.setRecipient(it)
            }
        }
    }

    private fun fetchData() {
        viewModel.loadRecipient(args.recipientId).observe(viewLifecycleOwner) {
            log(it.toString())
            binding.model = it
            if (!args.phoneNumber.isNullOrBlank()) {
                binding.btnRecipientContacts.visibility = View.GONE
                it.setPhoneNumber(args.phoneNumber!!)
            }
            binding.executePendingBindings()
        }
    }

    private fun observeEvents() {
        binding.toolbar.setNavigationOnClickListener { closeDialog(RESULT_CANCEL) }

        viewModel.savedEvent.observe(viewLifecycleOwner) { closeDialog(RESULT_OK) }

        viewModel.selectContactEvent.observe(viewLifecycleOwner) { selectContact() }

        viewModel.addToGroupEvent.observe(viewLifecycleOwner) { selectGroups(it) }

        viewModel.removeGroupEvent.observe(viewLifecycleOwner) { removeRecipientGroupView(it) }

        viewModel.addGroupViewsEvent.observe(viewLifecycleOwner) { groups ->
            updateAllRecipientGroupViews(groups)
        }
    }

    private fun selectGroups(groupIds: IntArray) {
        hideKeyboard()
        getNavigationResult<List<Int>>(REQ_MULTI_SELECT_RECIPIENT_GROUP)?.observe(
            viewLifecycleOwner
        ) { ids ->
            if (!ids.isNullOrEmpty()) {
                viewModel.setGroups(ids)
            }
            removeNavigationResult<List<Int>>(REQ_MULTI_SELECT_RECIPIENT_GROUP)
        }
        navigate {
            EditableRecipientDialogDirections.actionOpenMultiSelectRecipientGroup(groupIds)
        }
    }

    private fun updateAllRecipientGroupViews(groups: List<RecipientGroup>) {
        removeAllRecipientGroupViews()
        groups.forEach(::addRecipientGroupView)
    }

    private fun selectContact() {
        hideKeyboard()
        ContactsManager.openContactsApp(requireContext(), contactsLauncher)
    }

    private fun removeAllRecipientGroupViews() {
        binding.groupsLayout.removeAllViews()
    }

    private fun removeRecipientGroupView(view: View) {
        val parent = view.parent as ViewGroup
        binding.groupsLayout.removeView(parent)
    }

    private fun addRecipientGroupView(group: RecipientGroup) {
        val inflater = LayoutInflater.from(requireContext())
        val recipientBinding = ViewRecipientGroupHolderBinding.inflate(
            inflater,
            binding.groupsLayout,
            true
        )

        recipientBinding.model = group
        recipientBinding.viewModel = viewModel
        recipientBinding.executePendingBindings()
    }

    override fun isDataEdited() = viewModel.isRecipientEdited()
}