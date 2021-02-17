package com.leviancode.android.gsmbox.ui.recipients.view

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
        binding.adapter = RecipientGroupExpandableListAdapter(
            requireContext(),
            viewModel
        )
        observeUI()
    }

    private fun observeUI() {
        binding.recipientExpandableList.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY < 0) {
                //  binding.fabMenuRecipients.showMenuButton(true)
            } else if (scrollY > 0) {
                //  binding.fabMenuRecipients.hideMenuButton(true)
            }
        }

        viewModel.groupsForExpandableList.observe(viewLifecycleOwner){ list ->
            binding.adapter?.setData(list)
        }
    }
}