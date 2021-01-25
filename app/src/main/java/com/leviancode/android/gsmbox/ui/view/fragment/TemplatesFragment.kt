package com.leviancode.android.gsmbox.ui.view.fragment

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
import com.leviancode.android.gsmbox.ui.viewmodel.TemplatesViewModel

class TemplatesFragment : Fragment() {
    private lateinit var binding: FragmentTemplatesBinding
    private val viewModel by viewModels<TemplatesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_templates, container, false)
        binding.lifecycleOwner = this
        binding.adapter = TemplateListAdapter()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        observeUI()
    }

    private fun observeUI(){
        viewModel.groups.observe(viewLifecycleOwner){ groupList ->
            binding.adapter?.submitList(groupList)
        }
    }
}