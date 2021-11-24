package com.leviancode.android.gsmbox.ui.screens.templates.groups.list

import android.view.View

import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.FragmentTemplateGroupListBinding
import com.leviancode.android.gsmbox.databinding.ListItemTemplateGroupBinding
import com.leviancode.android.gsmbox.domain.entities.template.TemplateGroup
import com.leviancode.android.gsmbox.ui.base.GenericListAdapter
import com.leviancode.android.gsmbox.ui.dialogs.alertdialogs.DeleteConfirmationAlertDialog
import com.leviancode.android.gsmbox.ui.dialogs.ItemPopupMenu
import com.leviancode.android.gsmbox.ui.dialogs.ItemPopupMenu.Companion.DELETE
import com.leviancode.android.gsmbox.ui.dialogs.ItemPopupMenu.Companion.EDIT
import com.leviancode.android.gsmbox.utils.extensions.navigate
import com.leviancode.android.gsmbox.ui.base.BaseFragment
import com.leviancode.android.gsmbox.ui.entities.templates.TemplateGroupUI
import com.leviancode.android.gsmbox.utils.extensions.hideFabWhileScrolling
import org.koin.androidx.viewmodel.ext.android.viewModel

class TemplateGroupListFragment :
    BaseFragment<FragmentTemplateGroupListBinding>(R.layout.fragment_template_group_list) {
    private val viewModel: TemplateGroupListViewModel by viewModel()
    private val listAdapter =
        GenericListAdapter<TemplateGroupUI, ListItemTemplateGroupBinding>(R.layout.list_item_template_group) { item, binding ->
            binding.model = item
            binding.viewModel = viewModel
        }

    override var bottomNavViewVisibility: Int = View.VISIBLE

    override fun onCreated() {
        binding.viewModel = viewModel
        binding.recyclerView.adapter = listAdapter
        observeData()
        observeEvents()
    }

    private fun observeEvents() {
        binding.recyclerView.hideFabWhileScrolling(binding.fabAddGroup)

        viewModel.addGroupEvent.observe(viewLifecycleOwner) {
            openEditScreen(it)
        }
        viewModel.selectedGroupEvent.observe(viewLifecycleOwner) {
            openSelectedGroup(it)
        }
        viewModel.popupMenuEvent.observe(viewLifecycleOwner) {
            showPopup(it.first, it.second)
        }
    }

    private fun observeData() {
        viewModel.data.observe(viewLifecycleOwner) { list ->
            binding.tvListEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            listAdapter.submitList(list)
        }
    }

    private fun openSelectedGroup(group: TemplateGroupUI) {
        navigate {
            TemplateGroupListFragmentDirections.actionOpenGroupTemplates(
                group.id, group.getName()
            )
        }
    }

    private fun openEditScreen(groupId: Int) {
        navigate {
            TemplateGroupListFragmentDirections.actionOpenEditableGroup(groupId)
        }
    }

    private fun showPopup(view: View, group: TemplateGroupUI) {
        ItemPopupMenu(requireContext(), view).showEditDelete { result ->
            when (result) {
                EDIT -> openEditScreen(group.id)
                DELETE -> deleteGroup(group)
            }
        }
    }

    private fun deleteGroup(item: TemplateGroupUI) {
        DeleteConfirmationAlertDialog(requireContext()).show(
            getString(R.string.delete_group),
            getString(R.string.delete_group_confirmation)
        ) { confirmed ->
            if (confirmed) {
                viewModel.deleteGroup(item)
                if (!binding.fabAddGroup.isShown) binding.fabAddGroup.show()
            }
        }
    }
}