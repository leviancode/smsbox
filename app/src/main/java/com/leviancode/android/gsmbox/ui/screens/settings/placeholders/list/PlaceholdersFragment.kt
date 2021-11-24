package com.leviancode.android.gsmbox.ui.screens.settings.placeholders.list

import android.view.View
import androidx.lifecycle.lifecycleScope
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.entities.placeholders.PlaceholderData
import com.leviancode.android.gsmbox.databinding.FragmentPlaceholdersBinding
import com.leviancode.android.gsmbox.databinding.ListItemPlaceholderBinding
import com.leviancode.android.gsmbox.ui.base.BaseFragment
import com.leviancode.android.gsmbox.ui.base.GenericListAdapter
import com.leviancode.android.gsmbox.ui.dialogs.ItemPopupMenu
import com.leviancode.android.gsmbox.ui.dialogs.ItemPopupMenu.Companion.DELETE
import com.leviancode.android.gsmbox.ui.dialogs.ItemPopupMenu.Companion.EDIT
import com.leviancode.android.gsmbox.ui.dialogs.alertdialogs.DeleteConfirmationAlertDialog
import com.leviancode.android.gsmbox.ui.dialogs.alertdialogs.InfoDialog
import com.leviancode.android.gsmbox.ui.entities.placeholder.PlaceholderUI
import com.leviancode.android.gsmbox.utils.extensions.collect
import com.leviancode.android.gsmbox.utils.extensions.hideFabWhileScrolling
import com.leviancode.android.gsmbox.utils.extensions.navigate
import com.leviancode.android.gsmbox.utils.extensions.navigateBack
import com.leviancode.android.gsmbox.utils.logI
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaceholdersFragment :
    BaseFragment<FragmentPlaceholdersBinding>(R.layout.fragment_placeholders) {
    private val viewModel: PlaceholdersViewModel by viewModel()
    private val listAdapter =
        GenericListAdapter<PlaceholderUI, ListItemPlaceholderBinding>(R.layout.list_item_placeholder) { item, binding ->
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
        viewModel.placeholders.collect(this){
            listAdapter.submitList(it)
        }
    }

    private fun showPopupMenu(view: View, id: Int) {
        ItemPopupMenu(requireContext(), view).showEditDelete { result ->
            when (result) {
                EDIT -> openEditableDialog(id)
                DELETE -> showConfirmationDialog(id)
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