package com.leviancode.android.gsmbox.ui.recipients.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.RecipientGroupExpandableListAdapter
import com.leviancode.android.gsmbox.databinding.FragmentRecipientGroupListBinding
import com.leviancode.android.gsmbox.ui.recipients.viewmodel.RecipientsViewModel

class RecipientGroupListFragment : Fragment() {
    private lateinit var binding: FragmentRecipientGroupListBinding
    private val viewModel: RecipientsViewModel by viewModels({requireParentFragment()})
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
        viewModel.groupWithRecipients.observe(viewLifecycleOwner){ list ->
            binding.adapter?.data = list
        }
    }
}