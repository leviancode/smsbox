package com.leviancode.android.gsmbox.ui.fragments.recipients.groups.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.core.data.model.recipients.GroupWithRecipients
import com.leviancode.android.gsmbox.core.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.core.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.core.data.model.recipients.RecipientWithGroup
import com.leviancode.android.gsmbox.ui.fragments.recipients.groups.adapters.RecipientGroupExpandableListAdapter
import com.leviancode.android.gsmbox.databinding.FragmentRecipientGroupListBinding
import com.leviancode.android.gsmbox.ui.dialogs.ItemPopupMenu
import com.leviancode.android.gsmbox.ui.dialogs.alertdialogs.DeleteConfirmationAlertDialog
import com.leviancode.android.gsmbox.ui.fragments.recipients.viewpager.RecipientsPagerFragmentDirections
import com.leviancode.android.gsmbox.core.utils.REQ_MULTI_SELECT_RECIPIENT
import com.leviancode.android.gsmbox.core.utils.extensions.getNavigationResult
import com.leviancode.android.gsmbox.core.utils.extensions.navigate
import com.leviancode.android.gsmbox.core.utils.extensions.removeNavigationResult
import com.leviancode.android.gsmbox.core.utils.extensions.showToast
import com.leviancode.android.gsmbox.core.utils.showUndoSnackbar

class RecipientGroupListFragment : Fragment() {
    private lateinit var binding: FragmentRecipientGroupListBinding
    private val viewModel: RecipientGroupListViewModel by viewModels({requireParentFragment()})
    private lateinit var listAdapter: RecipientGroupExpandableListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipientGroupListBinding.inflate(
            inflater,
            container,
            false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listAdapter = RecipientGroupExpandableListAdapter(
            requireContext(),
            viewModel
        )
        binding.adapter = listAdapter
        observeEvents()
        fetchData()
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

    private fun fetchData() {
        viewModel.getGroupsWithRecipients().observe(viewLifecycleOwner) { list ->
            binding.tvListEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            listAdapter.data = list
        }
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
                showToast(getString(R.string.toast_add_to_group, item.group.getName()))
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

    private fun showEditableRecipientDialog(recipientId: Int) {
        navigate {
            RecipientsPagerFragmentDirections.actionOpenEditableRecipient(recipientId, null)
        }
    }

    private fun showEditableRecipientGroupDialog(groupId: Int) {
        navigate {
            RecipientsPagerFragmentDirections.actionOpenEditableRecipientGroup(groupId)
        }
    }
}