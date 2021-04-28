package com.leviancode.android.gsmbox.ui.recipients.recipients.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.model.recipients.RecipientWithGroups
import com.leviancode.android.gsmbox.databinding.FragmentRecipientListBinding
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu
import com.leviancode.android.gsmbox.ui.extra.alertdialogs.DeleteConfirmationAlertDialog
import com.leviancode.android.gsmbox.ui.recipients.recipients.adapters.RecipientListAdapter
import com.leviancode.android.gsmbox.ui.recipients.viewpager.RecipientsPagerFragmentDirections
import com.leviancode.android.gsmbox.utils.REQ_MULTI_SELECT_RECIPIENT_GROUP
import com.leviancode.android.gsmbox.utils.extensions.getNavigationResult
import com.leviancode.android.gsmbox.utils.extensions.navigate
import com.leviancode.android.gsmbox.utils.extensions.removeNavigationResult
import com.leviancode.android.gsmbox.utils.helpers.ItemDragHelperCallback
import com.leviancode.android.gsmbox.utils.helpers.ItemDragListener

class RecipientListFragment : Fragment(), ItemDragListener {
    private lateinit var binding: FragmentRecipientListBinding
    private val viewModel: RecipientListViewModel by viewModels({requireParentFragment()})
    private lateinit var listAdapter: RecipientListAdapter
  //  private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipientListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listAdapter = RecipientListAdapter(viewModel)
        binding.adapter = listAdapter
       /* itemTouchHelper = ItemTouchHelper(ItemDragHelperCallback(this)).apply {
            attachToRecyclerView(binding.recipientsRecyclerView)
        }*/

        fetchData()
        observeEvents()
    }

    private fun fetchData() {
        viewModel.getRecipients().observe(viewLifecycleOwner){ list ->
            binding.tvListEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            listAdapter.submitList(list)
            listAdapter.notifyDataSetChanged()
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
        binding.recipientsRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && fab.isShown) fab.hide()
                else if (dy < 0 && !fab.isShown) fab.show()
            }
        })
    }


    private fun showRecipientPopupMenu(view: View, item: RecipientWithGroups) {
        ItemPopupMenu(requireContext(), view).showEditAddToGroupDelete { result ->
            when (result) {
                ItemPopupMenu.EDIT -> showEditableRecipientDialog(item.recipient.recipientId)
                ItemPopupMenu.ADD -> showSelectRecipientGroupDialog(item)
                ItemPopupMenu.DELETE -> deleteRecipient(item.recipient)
            }
        }
    }

    private fun showSelectRecipientGroupDialog(item: RecipientWithGroups) {
        getNavigationResult<List<Int>>(REQ_MULTI_SELECT_RECIPIENT_GROUP)?.observe(
            viewLifecycleOwner
        ) { ids ->
            if (!ids.isNullOrEmpty()) {
                viewModel.addRecipientToGroups(item.recipient, ids)
            }
            removeNavigationResult<List<Int>>(REQ_MULTI_SELECT_RECIPIENT_GROUP)
        }

        navigate {
            RecipientsPagerFragmentDirections.actionMultiSelectRecipientGroup(item.getGroupIds())
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

    private fun showEditableRecipientDialog(recipientId: Int) {
        navigate {
            RecipientsPagerFragmentDirections.actionOpenEditableRecipient(recipientId, null)
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Pair<Int, Int> {
        return listAdapter.moveItems(fromPosition, toPosition)
    }

    override fun onMoveFinished(firstId: Int, secondId: Int) {
        viewModel.updateRecipientsOrder(listAdapter.currentList)
    }

}