package com.leviancode.android.gsmbox.ui.recipients.view.recipients.edit

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
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.databinding.DialogEditableRecipientBinding
import com.leviancode.android.gsmbox.databinding.DialogEditableRecipientGroupHolderBinding
import com.leviancode.android.gsmbox.ui.templates.view.AbstractFullScreenDialog
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
        viewModel.loadRecipient(args.recipientId)
        setTitle(args.recipientId != 0L)
        if (!args.phoneNumber.isNullOrBlank()) {
            binding.btnRecipientContacts.visibility = View.GONE
            viewModel.setPhoneNumber(args.phoneNumber!!)
        }

        setupContactPickerLauncher()
        showKeyboard(binding.editTextRecipientName)
        observeUI()
    }

    private fun setTitle(editMode: Boolean) {
        if (editMode) binding.toolbar.title = getString(R.string.edit_recipient)
    }

    private fun setupContactPickerLauncher() {
        contactsLauncher = registerForActivityResult(PickContact()) { result ->
            ContactsManager.parseUri(requireContext(), result)?.let {
                viewModel.setContact(it)
            }
        }
    }

    private fun observeUI() {
      //  setTextUniqueWatcher()

        binding.toolbar.setNavigationOnClickListener { closeDialog(RESULT_CANCEL) }

        viewModel.savedEvent.observe(viewLifecycleOwner) { closeDialog(RESULT_OK) }

        viewModel.selectContactEvent.observe(viewLifecycleOwner) { selectContact() }

        viewModel.addToGroupEvent.observe(viewLifecycleOwner) { selectGroups(it) }

        viewModel.removeGroupEvent.observe(viewLifecycleOwner) { removeRecipientGroupView(it) }

        viewModel.addGroupViewsEvent.observe(viewLifecycleOwner) {
            it.forEach(::addRecipientGroupView)
        }
    }

   /* private fun setTextUniqueWatcher() {
        val textWatcher = TextUniqueWatcher { isUnique ->
            viewModel.data.recipient.isNameUnique = isUnique
        }
        binding.editTextRecipientName.addTextChangedListener(textWatcher)
        viewModel.recipientNamesWithoutCurrent(args.recipient.recipientId)
            .observe(viewLifecycleOwner) {
                textWatcher.wordList = it
            }
    }*/

    private fun selectGroups(recipientId: Long) {
        hideKeyboard()
        getNavigationResult<List<RecipientGroup>>(REQ_MULTI_SELECT_RECIPIENT_GROUP)?.observe(
            viewLifecycleOwner
        ) { result ->
            if (result != null) {
                viewModel.setGroups(result)
                updateAllRecipientGroupViews(result)
            }
            removeNavigationResult<List<RecipientGroup>>(REQ_MULTI_SELECT_RECIPIENT_GROUP)
        }
        navigate {
            EditableRecipientDialogDirections.actionOpenMultiSelectRecipientGroup(recipientId)
        }
    }

    private fun updateAllRecipientGroupViews(result: List<RecipientGroup>) {
        removeAllRecipientGroupViews()
        result.forEach(::addRecipientGroupView)
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
        hideKeyboard()
        super.onPause()
    }
}