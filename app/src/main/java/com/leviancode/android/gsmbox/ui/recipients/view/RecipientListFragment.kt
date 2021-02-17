package com.leviancode.android.gsmbox.ui.recipients.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.RecipientListAdapter
import com.leviancode.android.gsmbox.databinding.FragmentRecipientListBinding
import com.leviancode.android.gsmbox.ui.recipients.viewmodel.RecipientsViewModel

class RecipientListFragment : Fragment() {
    private lateinit var binding: FragmentRecipientListBinding
    private val viewModel: RecipientsViewModel by viewModels({requireParentFragment()})

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
        binding.adapter = RecipientListAdapter(viewModel)

        observeUI()
    }

    private fun observeUI() {
        binding.recipientsRecyclerView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY < 0) {
                //  binding.fabMenuRecipients.showMenuButton(true)
            } else if (scrollY > 0) {
                //  binding.fabMenuRecipients.hideMenuButton(true)
            }
        }

        viewModel.recipients.observe(viewLifecycleOwner){
            binding.adapter?.submitList(it)
            binding.adapter?.notifyDataSetChanged()
        }


    }
}