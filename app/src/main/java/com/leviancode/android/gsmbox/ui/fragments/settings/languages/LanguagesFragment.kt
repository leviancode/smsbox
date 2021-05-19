package com.leviancode.android.gsmbox.ui.fragments.settings.languages

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.leviancode.android.gsmbox.databinding.FragmentLanguagesBinding
import com.leviancode.android.gsmbox.ui.activities.MainActivity
import com.leviancode.android.gsmbox.core.utils.extensions.goBack
import com.leviancode.android.gsmbox.core.utils.managers.LanguageManager
import kotlinx.coroutines.launch

class LanguagesFragment : Fragment() {
    private lateinit var listAdapter: ArrayAdapter<String>
    private lateinit var binding: FragmentLanguagesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLanguagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLanguageList()
        observeEvents()
    }

    private fun observeEvents() {
        binding.languagesListView.setOnItemClickListener { parent, view, position, id ->
            val selectedLang = listAdapter.getItem(position) ?: return@setOnItemClickListener
            setDefaultLanguage(selectedLang)
        }
        binding.toolbar.setNavigationOnClickListener { goBack() }
    }

    private fun setupLanguageList() {
        val languages = LanguageManager.getLanguages(requireContext())
        listAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_single_choice, languages)
        binding.languagesListView.adapter = listAdapter
        val index = LanguageManager.getCurrentCodeIndex(requireContext())
        if (index != -1){
            binding.languagesListView.setItemChecked(index, true)
        }
    }

    private fun setDefaultLanguage(lang: String) {
        lifecycleScope.launch {
           LanguageManager.setDefaultLanguage(requireContext(), lang)
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }
    }
}