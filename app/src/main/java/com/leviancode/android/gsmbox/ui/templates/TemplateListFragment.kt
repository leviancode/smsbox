package com.leviancode.android.gsmbox.ui.templates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.TemplateListAdapter
import com.leviancode.android.gsmbox.databinding.FragmentTemplatesBinding

class TemplateListFragment : Fragment() {
    private lateinit var binding: FragmentTemplatesBinding
    private val viewModel by viewModels<TemplateListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_templates, container, false)
        binding.lifecycleOwner = this
        binding.adapter = TemplateListAdapter()
      //  requireActivity().setActionBar(requireActivity().findViewById(R.id.toolbar))


        observeUI()
        return binding.root
    }

    private fun observeUI(){
        viewModel.groups.observe(viewLifecycleOwner){ groupList ->
            binding.adapter?.submitList(groupList)
        }
    }
}