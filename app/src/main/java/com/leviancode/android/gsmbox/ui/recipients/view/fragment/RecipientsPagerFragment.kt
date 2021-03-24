package com.leviancode.android.gsmbox.ui.recipients.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.RecipientsViewPagerAdapter
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.databinding.FragmentRecipientsPagerBinding
import com.leviancode.android.gsmbox.ui.extra.alertdialogs.DeleteConfirmationAlertDialog
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu.Companion.ADD
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu.Companion.CLEAR
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu.Companion.DELETE
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu.Companion.EDIT
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu.Companion.REMOVE
import com.leviancode.android.gsmbox.ui.recipients.viewmodel.RecipientsViewModel
import com.leviancode.android.gsmbox.utils.*
import com.leviancode.android.gsmbox.utils.extensions.getNavigationResult
import com.leviancode.android.gsmbox.utils.extensions.navigate
import com.leviancode.android.gsmbox.utils.extensions.removeNavigationResult

class RecipientsPagerFragment : Fragment() {
    private lateinit var binding: FragmentRecipientsPagerBinding
    private val viewModel: RecipientsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_recipients_pager, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.groupMode = false
        setupViewPager()
        observeUI()
    }

    private fun setupViewPager() {
        binding.recipientsViewPager.adapter = RecipientsViewPagerAdapter(this)
        TabLayoutMediator(
            binding.tabLayoutRecipient,
            binding.recipientsViewPager
        ) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.all)
                else -> getString(R.string.groups)
            }
        }.attach()

        binding.tabLayoutRecipient.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position){
                    0 -> binding.groupMode = false
                    1 -> binding.groupMode = true
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })
    }

    private fun observeUI() {
        viewModel.addGroupEvent.observe(viewLifecycleOwner) {
            showEditableRecipientGroupDialog(it)
        }

        viewModel.addRecipientEvent.observe(viewLifecycleOwner) {
            showEditableRecipientDialog(it)
        }

        viewModel.recipientPopupMenuEvent.observe(viewLifecycleOwner) {
            showRecipientPopupMenu(it.first, it.second)
        }
        viewModel.recipientInGroupPopupMenuEvent.observe(viewLifecycleOwner) {
            showRecipientInGroupPopupMenu(it.first, it.second)
        }

        viewModel.groupPopupMenuEvent.observe(viewLifecycleOwner) {
            showGroupPopupMenu(it.first, it.second)
        }
    }

    private fun showRecipientPopupMenu(view: View, recipient: Recipient) {
        ItemPopupMenu(requireContext(), view).showEditAddToGroupDelete { result ->
            when (result) {
                EDIT -> showEditableRecipientDialog(recipient)
                ADD -> showSelectRecipientGroupDialog(recipient)
                DELETE -> deleteRecipient(recipient)
            }
        }
    }

    private fun showRecipientInGroupPopupMenu(view: View, recipient: Recipient) {
        ItemPopupMenu(requireContext(), view).showEditRemoveDelete { result ->
            when (result) {
                EDIT -> showEditableRecipientDialog(recipient)
                REMOVE -> removeRecipientFromGroup(recipient)
                DELETE -> deleteRecipient(recipient)
            }
        }
    }

    private fun showGroupPopupMenu(view: View, group: RecipientGroup) {
        ItemPopupMenu(requireContext(), view).showEditAddRecipientClearDelete { result ->
            when (result) {
                EDIT -> showEditableRecipientGroupDialog(group)
                ADD -> showSelectRecipientsDialog(group)
                CLEAR -> clearGroup(group)
                DELETE -> deleteGroup(group)
            }
        }
    }

    private fun showSelectRecipientGroupDialog(recipient: Recipient) {
        getNavigationResult<List<RecipientGroup>>(REQ_MULTI_SELECT_RECIPIENT_GROUP)?.observe(
            viewLifecycleOwner
        ) { result ->
            if (result != null) {
                viewModel.addRecipientToGroups(recipient, result)
                removeNavigationResult<String>(REQ_MULTI_SELECT_RECIPIENT_GROUP)
            }
        }

        navigate {
            RecipientsPagerFragmentDirections.actionMultiSelectRecipientGroup(recipient.recipientId)
        }
    }

    private fun showSelectRecipientsDialog(group: RecipientGroup) {
        getNavigationResult<List<Recipient>>(REQ_MULTI_SELECT_RECIPIENT)?.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                viewModel.addRecipientsToGroup(result, group)
                showToast(requireContext(), getString(R.string.toast_add_to_group, group.getRecipientGroupName()))
                removeNavigationResult<List<Recipient>>(REQ_MULTI_SELECT_RECIPIENT)
            }
        }
        navigate {
            RecipientsPagerFragmentDirections.actionMultiSelectRecipient(group.recipientGroupId)
        }
    }

    private fun removeRecipientFromGroup(recipient: Recipient) {
        viewModel.removeRecipientFromGroup(recipient)
    }

    private fun clearGroup(group: RecipientGroup) {
        viewModel.clearGroup(group)
        showUndoSnackbar(requireView(), getString(R.string.group_cleared)){
            viewModel.restoreGroupWithRecipients()
        }
    }

    private fun deleteRecipient(item: Recipient) {
        DeleteConfirmationAlertDialog(requireContext()).apply {
            title = getString(R.string.delete_recipient)
            message = getString(R.string.delete_recipient_confirmation)
            show { result ->
                if (result) viewModel.deleteRecipient(item)
            }
        }
    }

    private fun deleteGroup(item: RecipientGroup) {
        DeleteConfirmationAlertDialog(requireContext()).apply {
            title = getString(R.string.delete_group)
            message = getString(R.string.delete_group_confirmation)
        }.show { result ->
            if (result) viewModel.deleteGroup(item)
        }
    }

    private fun showEditableRecipientGroupDialog(group: RecipientGroup) {
        navigate {
            RecipientsPagerFragmentDirections.actionOpenEditableRecipientGroup(group)
        }
    }

    private fun showEditableRecipientDialog(recipient: Recipient) {
        navigate {
            RecipientsPagerFragmentDirections.actionOpenEditableRecipient(recipient)
        }
    }
}