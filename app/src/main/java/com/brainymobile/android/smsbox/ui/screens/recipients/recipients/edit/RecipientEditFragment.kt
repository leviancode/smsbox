package com.brainymobile.android.smsbox.ui.screens.recipients.recipients.edit

import android.Manifest
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.PickContact
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.brainymobile.android.smsbox.R
import com.brainymobile.android.smsbox.databinding.FragmentRecipientEditBinding
import com.brainymobile.android.smsbox.databinding.ViewRecipientGroupHolderBinding
import com.brainymobile.android.smsbox.ui.base.BaseFragment
import com.brainymobile.android.smsbox.ui.entities.recipients.RecipientGroupUI
import com.brainymobile.android.smsbox.ui.screens.recipients.groups.select.RecipientGroupMultiSelectListDialog
import com.brainymobile.android.smsbox.utils.REQ_SAVE_RECIPIENT_ID
import com.brainymobile.android.smsbox.utils.extensions.askPermission
import com.brainymobile.android.smsbox.utils.extensions.observe
import com.brainymobile.android.smsbox.utils.extensions.setNavigationResult
import com.brainymobile.android.smsbox.utils.extensions.showToast
import com.brainymobile.android.smsbox.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipientEditFragment :
    BaseFragment<FragmentRecipientEditBinding>(R.layout.fragment_recipient_edit) {
    private val viewModel: RecipientEditViewModel by viewModels()
    private val args: RecipientEditFragmentArgs by navArgs()
    private lateinit var contactsLauncher: ActivityResultLauncher<Void?>

    override val showConfirmationDialogBeforeQuit: Boolean
        get() = !viewModel.isDataSavedOrNotChanged()

    override val showKeyboardOnStarted: Boolean = true

    override fun onCreated() {
        binding.viewModel = viewModel
        setTitle(args.recipientId != 0)
        setupContactPickerLauncher()
        observeData()
        observeEvents()
    }

    private fun setTitle(editMode: Boolean) {
        if (editMode) binding.toolbar.title = getString(R.string.edit_recipient)
    }

    private fun setupContactPickerLauncher() {
        contactsLauncher = registerForActivityResult(PickContact()) { result ->
            result?.let {
                binding
                viewModel.addContact(result)
            }
        }
    }

    private fun observeData() {
        viewModel.loadRecipient(args.recipientId, args.phoneNumber, args.recipientName)
            .observe(viewLifecycleOwner) {
                binding.model = it
                binding.executePendingBindings()
            }
    }

    private fun observeEvents() {
        binding.toolbar.setNavigationOnClickListener { close() }

        viewModel.apply {
            recipientSavedEvent.observe(viewLifecycleOwner) { id ->
                if (viewModel.isSaveFromTemplate) {
                    setResultAndClose(id)
                } else {
                    close()
                }
            }
            selectContactEvent.observe(viewLifecycleOwner) { selectContact() }

            addToGroupEvent.observe(viewLifecycleOwner) { selectGroups(it) }

            removeGroupEvent.observe(viewLifecycleOwner) { removeRecipientGroupView(it) }

            addGroupViewsEvent.observe(viewLifecycleOwner) { groups ->
                updateAllRecipientGroupViews(groups)
            }
        }
    }

    private fun setResultAndClose(id: Int) {
        setNavigationResult(REQ_SAVE_RECIPIENT_ID, id)
        close()
    }

    private fun selectGroups(groupIds: List<Int>) {
        hideKeyboard()
        RecipientGroupMultiSelectListDialog.show(childFragmentManager, groupIds) { selectedGroups ->
            viewModel.setGroups(selectedGroups)
        }
    }

    private fun updateAllRecipientGroupViews(groups: List<RecipientGroupUI>) {
        removeAllRecipientGroupViews()
        groups.forEach(::addRecipientGroupView)
    }

    private fun selectContact() {
        hideKeyboard()
        askPermission(Manifest.permission.READ_CONTACTS) { result ->
            if (result) contactsLauncher.launch(null)
            else showToast(getString(R.string.permission_dined))
        }
    }

    private fun removeAllRecipientGroupViews() {
        binding.groupsLayout.removeAllViews()
    }

    private fun removeRecipientGroupView(view: View) {
        val parent = view.parent as ViewGroup
        binding.groupsLayout.removeView(parent)
    }

    private fun addRecipientGroupView(group: RecipientGroupUI) {
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

}