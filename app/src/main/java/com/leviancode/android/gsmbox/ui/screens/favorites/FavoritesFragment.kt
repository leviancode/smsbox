package com.leviancode.android.gsmbox.ui.screens.favorites

import android.view.View
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.FragmentFavoritesBinding
import com.leviancode.android.gsmbox.databinding.ListItemTemplateBinding
import com.leviancode.android.gsmbox.ui.base.GenericListAdapter
import com.leviancode.android.gsmbox.ui.base.BaseFragment
import com.leviancode.android.gsmbox.ui.dialogs.ItemPopupMenu
import com.leviancode.android.gsmbox.ui.dialogs.alertdialogs.DeleteConfirmationAlertDialog
import com.leviancode.android.gsmbox.ui.entities.templates.TemplateUI
import com.leviancode.android.gsmbox.ui.screens.templates.templates.list.TemplateListViewModel
import com.leviancode.android.gsmbox.utils.extensions.navigate
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>(R.layout.fragment_favorites) {
    private val viewModel: TemplateListViewModel by viewModel()
    private val listAdapter =
        GenericListAdapter<TemplateUI, ListItemTemplateBinding>(R.layout.list_item_template) { item, binding ->
            binding.viewModel = viewModel
            binding.model = item
        }

    override var bottomNavViewVisibility: Int = View.VISIBLE

    override fun onCreated() {
        binding.recyclerView.adapter = listAdapter
        observeEvents()
    }

    private fun observeEvents() {
        viewModel.favoriteTemplates.observe(viewLifecycleOwner) { list ->
            binding.tvListEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            listAdapter.submitList(list)
        }

        viewModel.popupMenuEvent.observe(viewLifecycleOwner) { showPopup(it) }
    }

    private fun showPopup(pair: Pair<View, TemplateUI>) {
        ItemPopupMenu(requireContext(), pair.first).showEditDelete { result ->
            when (result) {
                ItemPopupMenu.EDIT -> showEditableTemplateDialog(pair.second)
                ItemPopupMenu.DELETE -> deleteTemplate(pair.second)
            }
        }
    }

    private fun deleteTemplate(item: TemplateUI) {
        DeleteConfirmationAlertDialog(requireContext()).show(
            getString(R.string.delete_template),
            getString(R.string.delete_template_confirmation)
        ) { confirmed ->
            if (confirmed) viewModel.deleteTemplate(item)
        }
    }

    private fun showEditableTemplateDialog(template: TemplateUI) {
        navigate {
            FavoritesFragmentDirections.actionOpenEditableTemplate(
                groupId = template.groupId,
                templateId = template.id
            )
        }
    }
}