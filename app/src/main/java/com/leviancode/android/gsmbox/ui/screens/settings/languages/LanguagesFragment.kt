package com.leviancode.android.gsmbox.ui.screens.settings.languages

import android.content.Intent
import android.widget.ArrayAdapter
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.FragmentLanguagesBinding
import com.leviancode.android.gsmbox.ui.activities.MainActivity
import com.leviancode.android.gsmbox.ui.base.BaseFragment
import com.leviancode.android.gsmbox.utils.extensions.navigateBack
import org.koin.androidx.viewmodel.ext.android.viewModel

class LanguagesFragment : BaseFragment<FragmentLanguagesBinding>(R.layout.fragment_languages) {
    private val viewModel: LanguagesViewModel by viewModel()
    private lateinit var listAdapter: ArrayAdapter<String>

    override fun onCreated() {
        setupLanguageList()
        observeEvents()
    }

    private fun observeEvents() {
        binding.languagesListView.setOnItemClickListener { parent, view, position, id ->
            val selectedLang = listAdapter.getItem(position) ?: return@setOnItemClickListener
            viewModel.setDefaultLanguage(selectedLang)
        }
        binding.toolbar.setNavigationOnClickListener { navigateBack() }
        viewModel.restartApp.observe(viewLifecycleOwner){
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun setupLanguageList() {
        val langList = viewModel.getLanguages()
        listAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_single_choice, langList)
        binding.languagesListView.adapter = listAdapter
        val index = viewModel.getCurrentLangIndex()
        if (index != -1){
            binding.languagesListView.setItemChecked(index, true)
        }
    }
}