package com.leviancode.android.gsmbox.ui.recipients.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.Recipient
import com.leviancode.android.gsmbox.data.model.RecipientGroup
import com.leviancode.android.gsmbox.databinding.FragmentRecipientsBinding
import com.leviancode.android.gsmbox.ui.extra.DeleteConfirmationDialog
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu
import com.leviancode.android.gsmbox.ui.recipients.viewmodel.RecipientsViewModel
import com.leviancode.android.gsmbox.utils.ADD
import com.leviancode.android.gsmbox.utils.CLEAR
import com.leviancode.android.gsmbox.utils.DELETE
import com.leviancode.android.gsmbox.utils.EDIT

class RecipientsFragment : Fragment() {
    private lateinit var binding: FragmentRecipientsBinding
    private val viewModel: RecipientsViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipients, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        openAllRecipientsList()
        observeUI()
    }

    private fun observeUI() {
        binding.tabLayoutRecipient.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                when(tab.position){
                    0 -> openAllRecipientsList()
                    1 -> openGroupList()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })

        viewModel.addGroupLiveEvent.observe(viewLifecycleOwner){
            showEditableRecipientGroupDialog(it)
        }

        viewModel.addRecipientLiveEvent.observe(viewLifecycleOwner){
            showEditableRecipientDialog(it)
        }

        viewModel.recipientPopupMenuLiveEvent.observe(viewLifecycleOwner){
            showRecipientPopupMenu(it.first, it.second)
        }

        viewModel.groupPopupMenuLiveEvent.observe(viewLifecycleOwner){
            showGroupPopupMenu(it.first, it.second)
        }
    }

    private fun openAllRecipientsList(){
        childFragmentManager.commit {
            replace(R.id.list_container, RecipientListFragment())
        }
    }

    private fun openGroupList(){
        childFragmentManager.commit {
            replace(R.id.list_container, RecipientGroupListFragment())
        }
    }

    private fun showRecipientPopupMenu(view: View, recipient: Recipient) {
        ItemPopupMenu(requireContext(),view).showSimple { result ->
            when (result) {
                EDIT -> showEditableRecipientDialog(recipient)
                DELETE -> deleteRecipient(recipient)
            }
        }
    }

    private fun showGroupPopupMenu(view: View, group: RecipientGroup) {
        ItemPopupMenu(requireContext(),view).showForRecipientGroup { result ->
            when (result) {
                ADD -> showEditableRecipientDialog(Recipient(groupName = group.groupName))
                EDIT -> showEditableRecipientGroupDialog(group)
                CLEAR -> clearGroup(group)
                DELETE -> deleteGroup(group)
            }
        }
    }

    private fun clearGroup(group: RecipientGroup) {
        viewModel.clearGroup(group)
    }

    private fun deleteRecipient(item: Recipient) {
        DeleteConfirmationDialog(requireContext()).apply {
            title = getString(R.string.delete_recipient)
            message = getString(R.string.delete_recipient_confirmation)
            show { result ->
                if (result) viewModel.deleteRecipient(item)
            }
        }
    }

    private fun deleteGroup(item: RecipientGroup) {
        DeleteConfirmationDialog(requireContext()).apply {
            title = getString(R.string.delete_group)
            message = getString(R.string.delete_group_confirmation)
            show { result ->
                if (result) viewModel.deleteGroup(item)
            }
        }
    }

    private fun showEditableRecipientGroupDialog(group: RecipientGroup) {
        findNavController().navigate(
            RecipientsFragmentDirections.actionOpenEditableRecipientGroup(group)
        )
    }

    private fun showEditableRecipientDialog(recipient: Recipient) {
        findNavController().navigate(
            RecipientsFragmentDirections.actionOpenEditableRecipient(recipient)
        )
    }
}