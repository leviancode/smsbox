package com.leviancode.android.gsmbox.ui.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.ListItemClickListener
import com.leviancode.android.gsmbox.adapters.TemplateListAdapter
import com.leviancode.android.gsmbox.databinding.FragmentFavoritesBinding
import com.leviancode.android.gsmbox.ui.viewmodel.TemplateListViewModel
import com.leviancode.android.gsmbox.utils.SmsManager


class FavoritesFragment : Fragment() {
    private val viewModel: TemplateListViewModel by viewModels()
    private lateinit var binding: FragmentFavoritesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.adapter = TemplateListAdapter().apply {
            clickListener = ListItemClickListener { template ->
                SmsManager.sendSms(requireContext(), template)
            }
        }
        observeUI()
    }

    private fun observeUI() {
        viewModel.templates.observe(viewLifecycleOwner){ templates ->
            binding.adapter?.submitList(templates.filter { it.favorite })
        }
    }
}