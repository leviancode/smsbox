package com.brainymobile.android.smsbox.ui.screens.recipients.recipients.list

import android.view.View
import androidx.fragment.app.viewModels
import com.brainymobile.android.smsbox.R
import com.brainymobile.android.smsbox.databinding.FragmentRecipientListBinding
import com.brainymobile.android.smsbox.databinding.ListItemRecipientBinding
import com.brainymobile.android.smsbox.ui.base.BaseFragment
import com.brainymobile.android.smsbox.ui.base.BaseListAdapter
import com.brainymobile.android.smsbox.ui.dialogs.PopupMenus
import com.brainymobile.android.smsbox.ui.dialogs.PopupMenus.MenuItem.*
import com.brainymobile.android.smsbox.ui.dialogs.alertdialogs.DeleteConfirmationAlertDialog
import com.brainymobile.android.smsbox.ui.entities.recipients.RecipientUI
import com.brainymobile.android.smsbox.ui.entities.recipients.RecipientWithGroupsUI
import com.brainymobile.android.smsbox.ui.screens.recipients.groups.select.RecipientGroupMultiSelectListDialog
import com.brainymobile.android.smsbox.ui.screens.recipients.viewpager.RecipientsPagerFragmentDirections
import com.brainymobile.android.smsbox.utils.extensions.hideFabWhileScrolling
import com.brainymobile.android.smsbox.utils.extensions.navigate
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipientListFragment :
    BaseFragment<FragmentRecipientListBinding>(R.layout.fragment_recipient_list) {
    private val viewModel: RecipientListViewModel by viewModels({ requireParentFragment() })
    private val listAdapter =
        BaseListAdapter<RecipientUI, ListItemRecipientBinding>(R.layout.list_item_recipient) { binding, item, position ->
            binding.viewModel = viewModel
            binding.model = item
        }

    override var bottomNavViewVisibility: Int = View.VISIBLE

    override fun onCreated() {
        binding.recyclerView.adapter = listAdapter
        listAdapter.initDragNDrop(binding.recyclerView) {
            viewModel.updateRecipients(listAdapter.currentList)
        }
        observeData()
        observeEvents()
    }

    private fun observeData() {
        viewModel.recipients.observe(viewLifecycleOwner) { list ->
            binding.tvListEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            listAdapter.submitList(list)
        }
    }

    private fun observeEvents() {
        viewModel.addRecipientEvent.observe(viewLifecycleOwner) {
            showEditableRecipientDialog(it)
        }

        viewModel.recipientPopupMenuEvent.observe(viewLifecycleOwner) {
            showRecipientPopupMenu(it.first, it.second)
        }

        val fab = requireParentFragment().requireView()
            .findViewById<FloatingActionButton>(R.id.fab_recipients)
        binding.recyclerView.hideFabWhileScrolling(fab)
    }


    private fun showRecipientPopupMenu(view: View, item: RecipientWithGroupsUI) {
        PopupMenus(view).showEditAddToGroupDelete { result ->
            when (result) {
                EDIT -> showEditableRecipientDialog(item.recipient.id)
                ADD -> showSelectRecipientGroupDialog(item)
                DELETE -> deleteRecipient(item.recipient)
                else -> {}
            }
        }
    }

    private fun showSelectRecipientGroupDialog(item: RecipientWithGroupsUI) {
        RecipientGroupMultiSelectListDialog.show(
            childFragmentManager,
            item.getGroupsIds()
        ) { selectedGroups ->
            viewModel.addRecipientToGroups(item.recipient, selectedGroups)
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

    private fun showEditableRecipientDialog(recipientId: Int) {
        navigate {
            RecipientsPagerFragmentDirections.actionOpenEditableRecipient(
                recipientId = recipientId,
                phoneNumber = null
            )
        }
    }

}