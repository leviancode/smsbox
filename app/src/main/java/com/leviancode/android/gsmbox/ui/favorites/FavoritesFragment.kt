package com.leviancode.android.gsmbox.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.ui.templates.templates.adapters.TemplateListAdapter
import com.leviancode.android.gsmbox.data.model.templates.Template
import com.leviancode.android.gsmbox.databinding.FragmentFavoritesBinding
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu
import com.leviancode.android.gsmbox.ui.extra.alertdialogs.DeleteConfirmationAlertDialog
import com.leviancode.android.gsmbox.ui.templates.templates.list.TemplateListViewModel
import com.leviancode.android.gsmbox.utils.extensions.navigate


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
        viewModel.favoriteTemplates.observe(viewLifecycleOwner) { list ->
            binding.tvListEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            listAdapter.submitList(list)
        }

        viewModel.popupMenuLiveEvent.observe(viewLifecycleOwner) { showPopup(it) }
    }

    private fun showPopup(pair: Pair<View, Template>) {
        ItemPopupMenu(requireContext(), pair.first).showEditDelete { result ->
            when (result) {
                ItemPopupMenu.EDIT -> showEditableTemplateDialog(pair.second)
                ItemPopupMenu.DELETE -> deleteTemplate(pair.second)
            }
        }
    }

    private fun deleteTemplate(item: Template) {
        DeleteConfirmationAlertDialog.show(
            requireContext(),
            getString(R.string.delete_template),
            getString(R.string.delete_template_confirmation)
        ) { confirmed ->
            if (confirmed) viewModel.deleteTemplate(item)
        }
    }

    private fun showEditableTemplateDialog(template: Template) {
        navigate {
            FavoritesFragmentDirections.actionOpenEditableTemplate(
                groupId = template.templateGroupId,
                templateId = template.templateId
            )
        }
    }
}