package com.leviancode.android.gsmbox.ui.screens.recipients.groups.list

import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.FragmentRecipientGroupListBinding
import com.leviancode.android.gsmbox.ui.base.BaseFragment
import com.leviancode.android.gsmbox.ui.dialogs.PopupMenus
import com.leviancode.android.gsmbox.ui.dialogs.PopupMenus.MenuItem.*
import com.leviancode.android.gsmbox.ui.dialogs.alertdialogs.DeleteConfirmationAlertDialog
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientGroupUI
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientUI
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientWithGroupUI
import com.leviancode.android.gsmbox.ui.screens.recipients.groups.edit.RecipientGroupEditDialog
import com.leviancode.android.gsmbox.ui.screens.recipients.recipients.dialog.RecipientMultiSelectListDialog
import com.leviancode.android.gsmbox.ui.screens.recipients.viewpager.RecipientsPagerFragmentDirections
import com.leviancode.android.gsmbox.utils.extensions.hideFabWhileScrolling
import com.leviancode.android.gsmbox.utils.extensions.navigate
import com.leviancode.android.gsmbox.utils.extensions.showSnackbar
import com.leviancode.android.gsmbox.utils.extensions.showSnackbarWithAction
import org.koin.androidx.viewmodel.ext.android.getViewModel

class RecipientGroupListFragment :
    BaseFragment<FragmentRecipientGroupListBinding>(R.layout.fragment_recipient_group_list) {
    private val viewModel: RecipientGroupListViewModel by lazy {
        requireParentFragment().getViewModel()
    }
    private val listAdapter: RecipientGroupExpandableListAdapter by lazy {
        RecipientGroupExpandableListAdapter(
            requireContext(),
            viewModel
        )
    }

    override var bottomNavViewVisibility: Int = View.VISIBLE

    override fun onCreated() {
        binding.recipientExpandableList.setAdapter(listAdapter)
        observeEvents()
        observeData()
    }

    private fun observeEvents() {
        viewModel.addGroupEvent.observe(viewLifecycleOwner) {
            showEditableRecipientGroupDialog(it)
        }

        viewModel.recipientPopupMenuEvent.observe(viewLifecycleOwner) {
            showRecipientPopupMenu(it.first, it.second)
        }

        viewModel.groupPopupMenuEvent.observe(viewLifecycleOwner) {
            showGroupPopupMenu(it.first, it.second)
        }

        val fab = requireParentFragment().requireView()
            .findViewById<FloatingActionButton>(R.id.fab_recipients)
        binding.recipientExpandableList.hideFabWhileScrolling(fab)
    }

    private fun observeData() {
        viewModel.recipientGroups.observe(viewLifecycleOwner) { list ->
            binding.tvListEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            listAdapter.data = list
        }
    }

    private fun showRecipientPopupMenu(view: View, item: RecipientWithGroupUI) {
        PopupMenus(view).showEditRemoveDelete { result ->
            when (result) {
                EDIT -> showEditableRecipientDialog(item.recipient.id)
                REMOVE -> removeRecipientFromGroup(item)
                DELETE -> deleteRecipient(item.recipient)
                else -> {}
            }
        }
    }

    private fun showGroupPopupMenu(view: View, item: RecipientGroupUI) {
        PopupMenus(view).showEditAddRecipientClearDelete { result ->
            when (result) {
                EDIT -> showEditableRecipientGroupDialog(item.id)
                ADD -> showSelectRecipientsDialog(item)
                CLEAR -> clearGroup(item)
                DELETE -> deleteGroup(item)
                else -> {}
            }
        }
    }

    private fun showSelectRecipientsDialog(item: RecipientGroupUI) {
        RecipientMultiSelectListDialog.show(childFragmentManager, item.id) { result ->
            item.updateRecipients(result)
            viewModel.updateGroupWithRecipients(item)
            showSnackbar(getString(R.string.toast_add_to_group, item.getName()))
        }
    }

    private fun removeRecipientFromGroup(item: RecipientWithGroupUI) {
        viewModel.removeRecipientFromGroup(item)
    }

    private fun clearGroup(item: RecipientGroupUI) {
        viewModel.clearGroup(item)
        showSnackbarWithAction(R.string.group_cleared, R.string.undo) {
            viewModel.restoreGroupWithRecipients()
        }
    }

    private fun deleteRecipient(item: RecipientUI) {
        DeleteConfirmationAlertDialog(requireContext()).show(
            getString(R.string.delete_recipient),
            getString(R.string.delete_recipient_confirmation)
        ) { confirmed ->
            if (confirmed) viewModel.deleteRecipient(item)
        }
    }

    private fun deleteGroup(item: RecipientGroupUI) {
        DeleteConfirmationAlertDialog(requireContext()).show(
            getString(R.string.delete_group),
            getString(R.string.delete_group_confirmation)
        ) { confirmed ->
            if (confirmed) viewModel.deleteGroup(item)
        }
    }

    private fun showEditableRecipientDialog(recipientId: Int) {
        navigate {
            RecipientsPagerFragmentDirections.actionOpenEditableRecipient(recipientId, null)
        }
    }

    private fun showEditableRecipientGroupDialog(groupId: Int) {
        RecipientGroupEditDialog.show(childFragmentManager, groupId, onDialogDismiss = {
            viewModel.createGroupDialogDismissed.call()
        })
    }
}