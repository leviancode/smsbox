package com.leviancode.android.gsmbox.ui.recipients.view.recipients.list

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
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.databinding.FragmentRecipientListBinding
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu
import com.leviancode.android.gsmbox.ui.extra.alertdialogs.DeleteConfirmationAlertDialog
import com.leviancode.android.gsmbox.ui.recipients.adapters.RecipientListAdapter
import com.leviancode.android.gsmbox.ui.recipients.view.RecipientsPagerFragmentDirections
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
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipient_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listAdapter = RecipientListAdapter(viewModel)
        binding.adapter = listAdapter
        itemTouchHelper = ItemTouchHelper(ItemDragHelperCallback(this)).apply {
            attachToRecyclerView(binding.recipientsRecyclerView)
        }

        observeUI()
    }

    private fun observeUI() {
        viewModel.recipients.observe(viewLifecycleOwner){
            listAdapter.submitList(it)
            listAdapter.notifyDataSetChanged()
        }

        viewModel.addRecipientEvent.observe(viewLifecycleOwner) {
            showEditableRecipientDialog(it)
        }

        viewModel.recipientPopupMenuEvent.observe(viewLifecycleOwner) {
            showRecipientPopupMenu(it.first, it.second)
        }

        val fab = requireParentFragment().requireView()
            .findViewById<FloatingActionButton>(R.id.fab_recipients)
        binding.recipientsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy >0 && fab.isShown) fab.hide()
                else if (dy <0 && !fab.isShown) fab.show()
            }
        })
    }


    private fun showRecipientPopupMenu(view: View, recipient: Recipient) {
        ItemPopupMenu(requireContext(), view).showEditAddToGroupDelete { result ->
            when (result) {
                ItemPopupMenu.EDIT -> showEditableRecipientDialog(recipient.recipientId)
                ItemPopupMenu.ADD -> showSelectRecipientGroupDialog(recipient)
                ItemPopupMenu.DELETE -> deleteRecipient(recipient)
            }
        }
    }

    private fun showSelectRecipientGroupDialog(recipient: Recipient) {
        getNavigationResult<List<RecipientGroup>>(REQ_MULTI_SELECT_RECIPIENT_GROUP)?.observe(
            viewLifecycleOwner
        ) { result ->
            if (result != null) {
                viewModel.addRecipientToGroups(recipient, result)
                removeNavigationResult<List<RecipientGroup>>(REQ_MULTI_SELECT_RECIPIENT_GROUP)
            }
        }

        navigate {
            RecipientsPagerFragmentDirections.actionMultiSelectRecipientGroup(recipient.recipientId)
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

    private fun showEditableRecipientDialog(recipientId: Long) {
        navigate {
            RecipientsPagerFragmentDirections.actionOpenEditableRecipient(recipientId)
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        listAdapter.moveItems(fromPosition, toPosition)
    }

    override fun onMoveFinished() {
        viewModel.updateRecipientsOrder(listAdapter.currentList)
    }
}