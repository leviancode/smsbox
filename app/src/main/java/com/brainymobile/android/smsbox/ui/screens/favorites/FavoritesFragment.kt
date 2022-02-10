package com.brainymobile.android.smsbox.ui.screens.favorites

import android.view.View
import androidx.fragment.app.viewModels
import com.brainymobile.android.smsbox.R
import com.brainymobile.android.smsbox.databinding.FragmentFavoritesBinding
import com.brainymobile.android.smsbox.databinding.ListItemTemplateBinding
import com.brainymobile.android.smsbox.ui.base.BaseFragment
import com.brainymobile.android.smsbox.ui.base.BaseListAdapter
import com.brainymobile.android.smsbox.ui.dialogs.PopupMenus
import com.brainymobile.android.smsbox.ui.dialogs.PopupMenus.MenuItem.DELETE
import com.brainymobile.android.smsbox.ui.dialogs.PopupMenus.MenuItem.EDIT
import com.brainymobile.android.smsbox.ui.dialogs.alertdialogs.DeleteConfirmationAlertDialog
import com.brainymobile.android.smsbox.ui.entities.templates.TemplateUI
import com.brainymobile.android.smsbox.ui.screens.templates.templates.list.TemplateListViewModel
import com.brainymobile.android.smsbox.utils.extensions.navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>(R.layout.fragment_favorites) {
    private val viewModel: TemplateListViewModel by viewModels()
    private val listAdapter =
        BaseListAdapter<TemplateUI, ListItemTemplateBinding>(R.layout.list_item_template) { binding, item, position ->
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
        PopupMenus(pair.first).showEditDelete { result ->
            when (result) {
                EDIT -> showEditableTemplateDialog(pair.second)
                DELETE -> deleteTemplate(pair.second)
                else -> {}
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