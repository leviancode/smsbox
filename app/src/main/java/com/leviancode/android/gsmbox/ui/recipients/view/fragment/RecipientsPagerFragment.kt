package com.leviancode.android.gsmbox.ui.recipients.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.RecipientsViewPagerAdapter
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.databinding.FragmentRecipientsPagerBinding
import com.leviancode.android.gsmbox.ui.extra.DeleteConfirmationDialog
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu
import com.leviancode.android.gsmbox.ui.recipients.viewmodel.RecipientsViewModel
import com.leviancode.android.gsmbox.utils.*

class RecipientsPagerFragment : Fragment() {
    private lateinit var binding: FragmentRecipientsPagerBinding
    private val viewModel: RecipientsViewModel by activityViewModels()

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
        setupViewPager()
        observeUI()
    }

    private fun setupViewPager() {
        binding.recipientsViewPager.adapter = RecipientsViewPagerAdapter(requireActivity())
        TabLayoutMediator(
            binding.tabLayoutRecipient,
            binding.recipientsViewPager
        ) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.all)
                else -> getString(R.string.groups)
            }
        }.attach()
    }

    private fun observeUI() {
        viewModel.addGroupLiveEvent.observe(viewLifecycleOwner) {
            showEditableRecipientGroupDialog(it)
        }

        viewModel.addRecipientLiveEvent.observe(viewLifecycleOwner) {
            showEditableRecipientDialog(it)
        }

        viewModel.recipientPopupMenuLiveEvent.observe(viewLifecycleOwner) {
            showRecipientPopupMenu(it.first, it.second)
        }

        viewModel.groupPopupMenuLiveEvent.observe(viewLifecycleOwner) {
            showGroupPopupMenu(it.first, it.second)
        }
    }

    private fun showRecipientPopupMenu(view: View, recipient: Recipient) {
        ItemPopupMenu(requireContext(), view).showSimple { result ->
            when (result) {
                EDIT -> showEditableRecipientDialog(recipient)
                DELETE -> deleteRecipient(recipient)
            }
        }
    }

    private fun showGroupPopupMenu(view: View, group: RecipientGroup) {
        ItemPopupMenu(requireContext(), view).showForRecipientGroup { result ->
            when (result) {
                ADD -> showSelectRecipientDialog(group)
                EDIT -> showEditableRecipientGroupDialog(group)
                CLEAR -> clearGroup(group)
                DELETE -> deleteGroup(group)
            }
        }
    }

    private fun showSelectRecipientDialog(group: RecipientGroup) {
        getNavigationResult<Recipient>(REQUEST_SELECT_RECIPIENT)?.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                result.groupName = group.groupName
                viewModel.updateRecipient(result)
                showToast(requireContext(), getString(R.string.toast_add_to_group, group.groupName))
                removeNavigationResult<Recipient>(REQUEST_SELECT_RECIPIENT)
            }
        }
        navigate {
            RecipientsPagerFragmentDirections.actionOpenSelectRecipient(null)
        }
    }

    private fun clearGroup(group: RecipientGroup) {
        viewModel.clearGroup(group)
        showUndoSnackbar(requireView(), getString(R.string.group_cleared)){
            viewModel.restoreRecipients()
        }
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