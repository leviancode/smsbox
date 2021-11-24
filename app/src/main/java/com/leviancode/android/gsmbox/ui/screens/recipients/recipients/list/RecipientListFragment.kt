package com.leviancode.android.gsmbox.ui.screens.recipients.recipients.list

import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.FragmentRecipientListBinding
import com.leviancode.android.gsmbox.ui.dialogs.ItemPopupMenu
import com.leviancode.android.gsmbox.ui.dialogs.alertdialogs.DeleteConfirmationAlertDialog
import com.leviancode.android.gsmbox.ui.screens.recipients.viewpager.RecipientsPagerFragmentDirections
import com.leviancode.android.gsmbox.utils.REQ_MULTI_SELECT_RECIPIENT_GROUP
import com.leviancode.android.gsmbox.databinding.ListItemRecipientBinding
import com.leviancode.android.gsmbox.ui.base.GenericListAdapter
import com.leviancode.android.gsmbox.ui.base.BaseFragment
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientGroupUI
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientUI
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientWithGroupsUI
import com.leviancode.android.gsmbox.utils.extensions.getNavigationResult
import com.leviancode.android.gsmbox.utils.extensions.hideFabWhileScrolling
import com.leviancode.android.gsmbox.utils.extensions.navigate
import com.leviancode.android.gsmbox.utils.extensions.removeNavigationResult
import org.koin.androidx.viewmodel.ext.android.getViewModel

class RecipientListFragment : BaseFragment<FragmentRecipientListBinding>(R.layout.fragment_recipient_list){
    private val viewModel: RecipientListViewModel  by lazy {
        requireParentFragment().getViewModel()
    }
    private val listAdapter =
        GenericListAdapter<RecipientUI, ListItemRecipientBinding>(R.layout.list_item_recipient) { item, binding ->
            binding.viewModel = viewModel
            binding.model = item
        }

    override var bottomNavViewVisibility: Int = View.VISIBLE

    override fun onCreated() {
        binding.recyclerView.adapter = listAdapter
        observeData()
        observeEvents()
    }

    private fun observeData() {
        viewModel.recipients.observe(viewLifecycleOwner){ list ->
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
        ItemPopupMenu(requireContext(), view).showEditAddToGroupDelete { result ->
            when (result) {
                ItemPopupMenu.EDIT -> showEditableRecipientDialog(item.recipient.id)
                ItemPopupMenu.ADD -> showSelectRecipientGroupDialog(item)
                ItemPopupMenu.DELETE -> deleteRecipient(item.recipient)
            }
        }
    }

    private fun showSelectRecipientGroupDialog(item: RecipientWithGroupsUI) {
        getNavigationResult<List<RecipientGroupUI>>(REQ_MULTI_SELECT_RECIPIENT_GROUP)?.observe(
            viewLifecycleOwner
        ) { groups ->
            if (!groups.isNullOrEmpty()) {
                viewModel.addRecipientToGroups(item.recipient, groups)
            }
            removeNavigationResult<List<RecipientGroupUI>>(REQ_MULTI_SELECT_RECIPIENT_GROUP)
        }

        navigate {
            RecipientsPagerFragmentDirections.actionMultiSelectRecipientGroup(item.getGroupsIds().toIntArray())
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
            RecipientsPagerFragmentDirections.actionOpenEditableRecipient(recipientId, null)
        }
    }

}