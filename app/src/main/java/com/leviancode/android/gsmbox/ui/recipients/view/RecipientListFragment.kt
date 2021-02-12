package com.leviancode.android.gsmbox.ui.recipients.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.RecipientListAdapter
import com.leviancode.android.gsmbox.data.model.Recipient
import com.leviancode.android.gsmbox.databinding.FragmentRecipientListBinding
import com.leviancode.android.gsmbox.ui.extra.DeleteConfirmationDialog
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu
import com.leviancode.android.gsmbox.ui.recipients.viewmodel.RecipientListViewModel
import com.leviancode.android.gsmbox.utils.DELETE
import com.leviancode.android.gsmbox.utils.EDIT

class RecipientListFragment : Fragment() {
    private val viewModel: RecipientListViewModel by viewModels()
    private lateinit var binding: FragmentRecipientListBinding

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
        binding.viewModel = viewModel
        binding.adapter = RecipientListAdapter(viewModel)
        observeUI()
    }

    private fun observeUI() {
        viewModel.recipients.observe(viewLifecycleOwner){
            binding.adapter?.submitList(it)
            binding.adapter?.notifyDataSetChanged()
        }

        viewModel.addRecipientLiveEvent.observe(viewLifecycleOwner){
            showEditableRecipientDialog(null)
        }

        viewModel.popupMenuLiveEvent.observe(viewLifecycleOwner){
            showPopupMenu(it)
        }
    }

    private fun showPopupMenu(pair: Pair<View, Recipient>) {
        ItemPopupMenu(requireContext(), pair.first).show { result ->
            when (result) {
                EDIT -> showEditableRecipientDialog(pair.second)
                DELETE -> deleteRecipient(pair.second)
            }
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

    private fun showEditableRecipientDialog(recipient: Recipient?) {
        findNavController().navigate(
            RecipientListFragmentDirections.actionOpenEditableRecipient(recipient)
        )
    }
}