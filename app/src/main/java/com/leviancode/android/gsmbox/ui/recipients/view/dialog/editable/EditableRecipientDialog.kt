package com.leviancode.android.gsmbox.ui.recipients.view.dialog.editable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.data.model.recipients.RecipientWithGroups
import com.leviancode.android.gsmbox.databinding.DialogEditableRecipientBinding
import com.leviancode.android.gsmbox.databinding.DialogEditableRecipientGroupHolderBinding
import com.leviancode.android.gsmbox.databinding.DialogEditableTemplateNumberHolderBinding
import com.leviancode.android.gsmbox.ui.recipients.viewmodel.EditableRecipientViewModel
import com.leviancode.android.gsmbox.ui.templates.view.dialog.AbstractFullScreenDialog
import com.leviancode.android.gsmbox.utils.*
import com.leviancode.android.gsmbox.utils.extensions.getNavigationResult
import com.leviancode.android.gsmbox.utils.extensions.navigate
import com.leviancode.android.gsmbox.utils.extensions.removeNavigationResult
import com.leviancode.android.gsmbox.utils.managers.ContactsManager

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
        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_editable_recipient, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        args.recipient.let { recipient ->
            viewModel.setRecipient(recipient)

            if (recipient.getRecipientName().isNotBlank()) {
                binding.toolbar.title = recipient.getRecipientName()
                loadRecipientGroupViews(recipient.recipientId)
            } else if (recipient.getPhoneNumber().isNotBlank()) {
                binding.btnRecipientContacts.visibility = View.GONE
            }

        }

        setupContactPickerLauncher()
        showKeyboard(binding.editTextRecipientName)
        observeUI()
    }

    private fun loadRecipientGroupViews(recipientId: String) {
        viewModel.loadRecipientWithGroupsById(recipientId).observe(viewLifecycleOwner) {
            it.groups.forEach { group ->
                addRecipientGroupView(group)
            }
        }
    }

    private fun setupContactPickerLauncher() {
        contactsLauncher = registerForActivityResult(PickContact()) { result ->
            ContactsManager.parseUri(requireContext(), result)?.let {
                viewModel.setContact(it)
            }
        }
    }

    private fun observeUI() {
        binding.toolbar.setNavigationOnClickListener { closeDialog(RESULT_CANCEL) }

        viewModel.savedEvent.observe(viewLifecycleOwner) { closeDialog(RESULT_OK) }

        viewModel.selectContactEvent.observe(viewLifecycleOwner) { selectContact() }

        viewModel.addToGroupEvent.observe(viewLifecycleOwner) { selectGroups(it) }

        viewModel.removeGroupEvent.observe(viewLifecycleOwner) { removeRecipientGroupView(it) }

    }

    private fun selectGroups(recipientId: String) {
        hideKeyboard()
        getNavigationResult<List<RecipientGroup>>(REQ_SELECT_RECIPIENT_GROUP)?.observe(
            viewLifecycleOwner
        ) { result ->
            if (result != null) {
                viewModel.setGroups(result)
                removeAllRecipientGroupViews()
                result.forEach(::addRecipientGroupView)
                removeNavigationResult<String>(REQ_SELECT_RECIPIENT_GROUP)
            }
        }
        navigate {
            EditableRecipientDialogDirections.actionOpenRecipientGroupList(recipientId)
        }
    }

    private fun selectContact() {
        hideKeyboard()
        ContactsManager.openContactsApp(requireContext(), contactsLauncher)
    }

    private fun removeAllRecipientGroupViews(){
        binding.groupsLayout.removeAllViews()
    }

    private fun removeRecipientGroupView(view: View) {
        val parent = view.parent as ViewGroup
        binding.groupsLayout.removeView(parent)
    }

    private fun addRecipientGroupView(group: RecipientGroup) {
        val inflater = LayoutInflater.from(requireContext())
        val recipientBinding = DataBindingUtil.inflate<DialogEditableRecipientGroupHolderBinding>(
            inflater,
            R.layout.dialog_editable_recipient_group_holder,
            binding.groupsLayout,
            true
        )

        recipientBinding.model = group
        recipientBinding.viewModel = viewModel
        recipientBinding.executePendingBindings()
    }

    override fun isDataEdited() = viewModel.isRecipientEdited()

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }
}