package com.leviancode.android.gsmbox.ui.view.fragment

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
import com.leviancode.android.gsmbox.data.model.RecipientObservable
import com.leviancode.android.gsmbox.databinding.FragmentRecipientListBinding
import com.leviancode.android.gsmbox.ui.viewmodel.RecipientListViewModel

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
        binding.adapter = RecipientListAdapter()
        binding.viewModel = viewModel
        observeUI()
    }

    private fun observeUI() {
        viewModel.recipientsLiveData.observe(viewLifecycleOwner){
            binding.adapter?.submitList(it)
        }

        viewModel.addRecipientLiveEvent.observe(viewLifecycleOwner){ showNewRecipientDialog() }
    }

    private fun showNewRecipientDialog() {
        findNavController().navigate(
            RecipientListFragmentDirections.actionNewRecipient(null)
        )
    }
}