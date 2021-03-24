package com.leviancode.android.gsmbox.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.TemplateListAdapter
import com.leviancode.android.gsmbox.data.model.templates.Template
import com.leviancode.android.gsmbox.databinding.FragmentFavoritesBinding
import com.leviancode.android.gsmbox.ui.templates.viewmodel.TemplateListViewModel
import com.leviancode.android.gsmbox.utils.managers.SmsManager
import kotlinx.coroutines.launch


class FavoritesFragment : Fragment() {
    private val viewModel: TemplateListViewModel by viewModels()
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var listAdapter: TemplateListAdapter

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
        listAdapter = TemplateListAdapter(viewModel)
        binding.adapter = listAdapter
        observeUI()
    }

    private fun observeUI() {
        viewModel.favoriteTemplates.observe(viewLifecycleOwner){ list ->
            binding.tvListEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            listAdapter.submitList(list)
        }
        viewModel.sendMessageLiveEvent.observe(viewLifecycleOwner){ sendMessage(it) }
    }

    private fun sendMessage(template: Template){
        lifecycleScope.launch {
            SmsManager.sendSms(requireContext(), template)
        }
    }
}