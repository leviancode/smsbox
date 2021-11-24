package com.leviancode.android.gsmbox.ui.screens.recipients.recipients.edit

import android.Manifest
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.PickContact
import androidx.navigation.fragment.navArgs
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.FragmentRecipientEditBinding
import com.leviancode.android.gsmbox.databinding.ViewRecipientGroupHolderBinding
import com.leviancode.android.gsmbox.ui.base.BaseFragment
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientGroupUI
import com.leviancode.android.gsmbox.utils.*
import com.leviancode.android.gsmbox.utils.extensions.*
import com.leviancode.android.gsmbox.utils.managers.ContactsManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipientEditFragment : BaseFragment<FragmentRecipientEditBinding>(R.layout.fragment_recipient_edit) {
    private val viewModel: RecipientEditViewModel by viewModel()
    private val args: RecipientEditFragmentArgs by navArgs()
    private lateinit var contactsLauncher: ActivityResultLauncher<Void>

    override val showConfirmationDialogBeforeQuit: Boolean
        get() = viewModel.isDataSavedOrNotChanged()

    override fun onCreated() {
        binding.viewModel = viewModel
        viewModel.setSaveFromTemplateMode(args.recipientId == 0 && !args.phoneNumber.isNullOrBlank())
        setTitle(args.recipientId != 0)
        setupContactPickerLauncher()
        showKeyboard()
        observeData()
        observeEvents()
    }

    private fun setTitle(editMode: Boolean) {
        if (editMode) binding.toolbar.title = getString(R.string.edit_recipient)
    }

    private fun setupContactPickerLauncher() {
        contactsLauncher = registerForActivityResult(PickContact()) { result ->
            viewModel.addContact(result)
        }
    }

    private fun observeData() {
        viewModel.loadRecipient(args.recipientId, args.phoneNumber).observe(viewLifecycleOwner) {
            binding.model = it
            binding.executePendingBindings()
        }
    }

    private fun observeEvents() {
        binding.toolbar.setNavigationOnClickListener { close() }

        viewModel.quitEvent.observe(viewLifecycleOwner) {
            if (viewModel.isSaveFromTemplateMode){
                setResultAndClose()
            } else {
                close()
            }
        }

        viewModel.selectContactEvent.observe(viewLifecycleOwner) { selectContact() }

        viewModel.addToGroupEvent.observe(viewLifecycleOwner) { selectGroups(it) }

        viewModel.removeGroupEvent.observe(viewLifecycleOwner) { removeRecipientGroupView(it) }

        viewModel.addGroupViewsEvent.observe(viewLifecycleOwner) { groups ->
            updateAllRecipientGroupViews(groups)
        }
    }

    private fun setResultAndClose() {
        setNavigationResult(viewModel.recipient, REQ_SAVE_RECIPIENT)
        close()
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
            RecipientEditFragmentDirections.actionOpenMultiSelectRecipientGroup(groupIds)
        }
    }

    private fun updateAllRecipientGroupViews(groups: List<RecipientGroupUI>) {
        removeAllRecipientGroupViews()
        groups.forEach(::addRecipientGroupView)
    }

    private fun selectContact() {
        hideKeyboard()
        askPermission(Manifest.permission.READ_CONTACTS){ result ->
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