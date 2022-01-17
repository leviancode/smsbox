package com.brainymobile.android.smsbox.ui.screens.templates.groups.list

import android.view.View
import com.brainymobile.android.smsbox.R
import com.brainymobile.android.smsbox.databinding.FragmentTemplateGroupListBinding
import com.brainymobile.android.smsbox.databinding.ListItemTemplateGroupBinding
import com.brainymobile.android.smsbox.ui.base.BaseFragment
import com.brainymobile.android.smsbox.ui.base.BaseListAdapter
import com.brainymobile.android.smsbox.ui.dialogs.PopupMenus
import com.brainymobile.android.smsbox.ui.dialogs.PopupMenus.MenuItem.DELETE
import com.brainymobile.android.smsbox.ui.dialogs.PopupMenus.MenuItem.EDIT
import com.brainymobile.android.smsbox.ui.dialogs.alertdialogs.DeleteConfirmationAlertDialog
import com.brainymobile.android.smsbox.ui.entities.templates.TemplateGroupUI
import com.brainymobile.android.smsbox.utils.extensions.hideFabWhileScrolling
import com.brainymobile.android.smsbox.utils.extensions.navigate
import com.brainymobile.android.smsbox.utils.extensions.observe
import com.brainymobile.android.smsbox.utils.logI
import org.koin.androidx.viewmodel.ext.android.viewModel

class TemplateGroupListFragment :
    BaseFragment<FragmentTemplateGroupListBinding>(R.layout.fragment_template_group_list) {
    private val viewModel: TemplateGroupListViewModel by viewModel()
    private val listAdapter =
        BaseListAdapter<TemplateGroupUI, ListItemTemplateGroupBinding>(R.layout.list_item_template_group) { binding, item, position ->
            binding.model = item
            binding.viewModel = viewModel
        }

    override var bottomNavViewVisibility: Int = View.VISIBLE

    override fun onCreated() {
        binding.viewModel = viewModel
        binding.recyclerView.adapter = listAdapter
        listAdapter.initDragNDrop(binding.recyclerView, ::onDragFinish)
        observeData()
        observeEvents()
    }

    private fun onDragFinish(){
        logI("drag finished")
        viewModel.updateAll(listAdapter.currentList)
    }

    override fun onPause() {
        super.onPause()
        binding.fabAddGroup.hide()
    }

    override fun onResume() {
        super.onResume()
        binding.fabAddGroup.show()
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
        PopupMenus(view).showEditDelete { result ->
            when (result) {
                EDIT -> openEditScreen(group.id)
                DELETE -> deleteGroup(group)
                else -> {}
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