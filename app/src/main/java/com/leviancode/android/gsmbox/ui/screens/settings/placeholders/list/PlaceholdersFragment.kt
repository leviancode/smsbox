package com.leviancode.android.gsmbox.ui.screens.settings.placeholders.list

import android.view.View
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.FragmentPlaceholdersBinding
import com.leviancode.android.gsmbox.databinding.ListItemPlaceholderBinding
import com.leviancode.android.gsmbox.ui.base.BaseFragment
import com.leviancode.android.gsmbox.ui.base.BaseListAdapter
import com.leviancode.android.gsmbox.ui.dialogs.PopupMenus
import com.leviancode.android.gsmbox.ui.dialogs.PopupMenus.MenuItem.DELETE
import com.leviancode.android.gsmbox.ui.dialogs.PopupMenus.MenuItem.EDIT
import com.leviancode.android.gsmbox.ui.dialogs.alertdialogs.DeleteConfirmationAlertDialog
import com.leviancode.android.gsmbox.ui.dialogs.alertdialogs.InfoDialog
import com.leviancode.android.gsmbox.ui.entities.placeholder.PlaceholderUI
import com.leviancode.android.gsmbox.utils.extensions.hideFabWhileScrolling
import com.leviancode.android.gsmbox.utils.extensions.navigate
import com.leviancode.android.gsmbox.utils.extensions.navigateBack
import com.leviancode.android.gsmbox.utils.extensions.observe
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaceholdersFragment :
    BaseFragment<FragmentPlaceholdersBinding>(R.layout.fragment_placeholders) {
    private val viewModel: PlaceholdersViewModel by viewModel()
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
        viewModel.placeholders.observe(this){
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
            getString(R.string.delete_placeholder),
            getString(R.string.delete_placeholder_confirmation)
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
        InfoDialog(requireContext()).show( getString(R.string.placeholders_info))
    }
}