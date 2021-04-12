package com.leviancode.android.gsmbox.ui.recipients.view.groups.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.recipients.GroupWithRecipients
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.data.model.recipients.RecipientWithGroup
import com.leviancode.android.gsmbox.ui.recipients.adapters.RecipientGroupExpandableListAdapter
import com.leviancode.android.gsmbox.databinding.FragmentRecipientGroupListBinding
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu
import com.leviancode.android.gsmbox.ui.extra.alertdialogs.DeleteConfirmationAlertDialog
import com.leviancode.android.gsmbox.ui.recipients.view.RecipientsPagerFragmentDirections
import com.leviancode.android.gsmbox.utils.REQ_MULTI_SELECT_RECIPIENT
import com.leviancode.android.gsmbox.utils.extensions.getNavigationResult
import com.leviancode.android.gsmbox.utils.extensions.navigate
import com.leviancode.android.gsmbox.utils.extensions.removeNavigationResult
import com.leviancode.android.gsmbox.utils.showToast
import com.leviancode.android.gsmbox.utils.showUndoSnackbar

class RecipientGroupListFragment : Fragment() {
    private lateinit var binding: FragmentRecipientGroupListBinding
    private val viewModel: RecipientGroupListViewModel by viewModels({requireParentFragment()})
    private lateinit var listAdapter: RecipientGroupExpandableListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_recipient_group_list,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listAdapter = RecipientGroupExpandableListAdapter(
            requireContext(),
            viewModel
        )
        binding.adapter = listAdapter
        observeUI()
    }

    private fun observeUI() {
        viewModel.groupsWithRecipients.observe(viewLifecycleOwner){ list ->
            binding.adapter?.data = list
        }

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
        binding.recipientExpandableList.setOnScrollListener(object : AbsListView.OnScrollListener {
            private var lastFirstVisibleItem = 0
            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {}

            override fun onScroll(
                view: AbsListView?,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {
                // Scroll Down
                if(lastFirstVisibleItem < firstVisibleItem && fab.isShown){
                    fab.hide()
                }
                // Scroll Up
                if(lastFirstVisibleItem > firstVisibleItem && !fab.isShown){
                    fab.show()
                }
                lastFirstVisibleItem = firstVisibleItem
            }

        })
    }

    private fun showRecipientPopupMenu(view: View, item: RecipientWithGroup) {
        ItemPopupMenu(requireContext(), view).showEditRemoveDelete { result ->
            when (result) {
                ItemPopupMenu.EDIT -> showEditableRecipientDialog(item.recipient.recipientId)
                ItemPopupMenu.REMOVE -> removeRecipientFromGroup(item)
                ItemPopupMenu.DELETE -> deleteRecipient(item.recipient)
            }
        }
    }

    private fun showGroupPopupMenu(view: View, item: GroupWithRecipients) {
        ItemPopupMenu(requireContext(), view).showEditAddRecipientClearDelete { result ->
            when (result) {
                ItemPopupMenu.EDIT -> showEditableRecipientGroupDialog(item.group.recipientGroupId)
                ItemPopupMenu.ADD -> showSelectRecipientsDialog(item)
                ItemPopupMenu.CLEAR -> clearGroup(item)
                ItemPopupMenu.DELETE -> deleteGroup(item.group)
            }
        }
    }

    private fun showSelectRecipientsDialog(item: GroupWithRecipients) {
        getNavigationResult<List<Recipient>>(REQ_MULTI_SELECT_RECIPIENT)?.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                item.setRecipients(result)
                viewModel.updateGroupWithRecipients(item)
                showToast(
                    requireContext(),
                    getString(R.string.toast_add_to_group, item.group.getName())
                )
                removeNavigationResult<List<Recipient>>(REQ_MULTI_SELECT_RECIPIENT)
            }
        }
        navigate {
            RecipientsPagerFragmentDirections.actionMultiSelectRecipient(item.group.recipientGroupId)
        }
    }

    private fun removeRecipientFromGroup(item: RecipientWithGroup) {
        viewModel.removeRecipientFromGroup(item)
    }

    private fun clearGroup(item: GroupWithRecipients) {
        viewModel.clearGroup(item)
        showUndoSnackbar(requireView(), getString(R.string.group_cleared)) {
            viewModel.restoreGroupWithRecipients()
        }
    }

    private fun deleteRecipient(item: Recipient) {
        DeleteConfirmationAlertDialog.show(
            requireContext(),
            getString(R.string.delete_recipient),
            getString(R.string.delete_recipient_confirmation)
        ) { confirmed ->
            if (confirmed) viewModel.deleteRecipient(item)
        }
    }

    private fun deleteGroup(item: RecipientGroup) {
        DeleteConfirmationAlertDialog.show(
            requireContext(),
            getString(R.string.delete_group),
            getString(R.string.delete_group_confirmation)
        ) { confirmed ->
            if (confirmed) viewModel.deleteGroup(item)
        }
    }

    private fun showEditableRecipientDialog(recipientId: Long) {
        navigate {
            RecipientsPagerFragmentDirections.actionOpenEditableRecipient(recipientId)
        }
    }

    private fun showEditableRecipientGroupDialog(groupId: Long) {
        navigate {
            RecipientsPagerFragmentDirections.actionOpenEditableRecipientGroup(groupId)
        }
    }
}