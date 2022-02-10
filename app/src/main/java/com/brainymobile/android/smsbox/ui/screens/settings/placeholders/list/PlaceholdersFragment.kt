package com.brainymobile.android.smsbox.ui.screens.settings.placeholders.list

import android.view.View
import androidx.fragment.app.viewModels
import com.brainymobile.android.smsbox.R
import com.brainymobile.android.smsbox.databinding.FragmentPlaceholdersBinding
import com.brainymobile.android.smsbox.databinding.ListItemPlaceholderBinding
import com.brainymobile.android.smsbox.ui.base.BaseFragment
import com.brainymobile.android.smsbox.ui.base.BaseListAdapter
import com.brainymobile.android.smsbox.ui.dialogs.PopupMenus
import com.brainymobile.android.smsbox.ui.dialogs.PopupMenus.MenuItem.DELETE
import com.brainymobile.android.smsbox.ui.dialogs.PopupMenus.MenuItem.EDIT
import com.brainymobile.android.smsbox.ui.dialogs.alertdialogs.DeleteConfirmationAlertDialog
import com.brainymobile.android.smsbox.ui.dialogs.alertdialogs.InfoDialog
import com.brainymobile.android.smsbox.ui.entities.placeholder.PlaceholderUI
import com.brainymobile.android.smsbox.utils.extensions.hideFabWhileScrolling
import com.brainymobile.android.smsbox.utils.extensions.navigate
import com.brainymobile.android.smsbox.utils.extensions.navigateBack
import com.brainymobile.android.smsbox.utils.extensions.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceholdersFragment :
    BaseFragment<FragmentPlaceholdersBinding>(R.layout.fragment_placeholders) {
    private val viewModel: PlaceholdersViewModel by viewModels()
    private val listAdapter =
        BaseListAdapter<PlaceholderUI, ListItemPlaceholderBinding>(R.layout.list_item_placeholder) { binding, item, position ->
            binding.viewModel = viewModel
            binding.model = item
        }

    override fun onCreated() {
        binding.recyclerView.adapter = listAdapter
        binding.viewModel = viewModel
        observeEvents()
        observeData()
    }

    private fun observeEvents() {
        binding.apply {
            recyclerView.hideFabWhileScrolling(fabAddPlaceholder)
            toolbarPlaceholders.setNavigationOnClickListener { navigateBack() }
            toolbarPlaceholders.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_info -> openInfoDialog()
                }
                true
            }
        }

        viewModel.addPlaceholderEvent.observe(viewLifecycleOwner) {
            openEditableDialog()
        }

        viewModel.onPopupMenuClickEvent.observe(viewLifecycleOwner) {
            showPopupMenu(it.first, it.second)
        }

    }

    private fun observeData() {
        viewModel.placeholders.observe(this) {
            listAdapter.submitList(it)
        }
    }

    private fun showPopupMenu(view: View, id: Int) {
        PopupMenus(view).showEditDelete { result ->
            when (result) {
                EDIT -> openEditableDialog(id)
                DELETE -> showConfirmationDialog(id)
                else -> {}
            }
        }
    }

    private fun showConfirmationDialog(id: Int) {
        DeleteConfirmationAlertDialog(requireContext()).show(
            getString(R.string.delete_variable),
            getString(R.string.delete_variable_confirmation)
        ) { confirmed ->
            if (confirmed) viewModel.deletePlaceholder(id)
        }
    }

    private fun openEditableDialog(id: Int = 0) {
        navigate {
            PlaceholdersFragmentDirections.actionOpenEditablePlaceholder(id)
        }
    }

    private fun openInfoDialog() {
        InfoDialog(requireContext()).show(getString(R.string.variables_info))
    }
}