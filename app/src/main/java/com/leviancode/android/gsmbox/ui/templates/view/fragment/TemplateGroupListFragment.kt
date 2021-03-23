package com.leviancode.android.gsmbox.ui.templates.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper

import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.TemplateGroupListAdapter
import com.leviancode.android.gsmbox.data.model.templates.TemplateGroup
import com.leviancode.android.gsmbox.databinding.FragmentTemplateGroupListBinding
import com.leviancode.android.gsmbox.helpers.ItemDragHelperCallback
import com.leviancode.android.gsmbox.helpers.ItemDragListener
import com.leviancode.android.gsmbox.ui.extra.alertdialogs.DeleteConfirmationAlertDialog
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu.Companion.DELETE
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu.Companion.EDIT
import com.leviancode.android.gsmbox.ui.templates.viewmodel.TemplateGroupListViewModel
import com.leviancode.android.gsmbox.utils.extensions.navigate

class TemplateGroupListFragment : Fragment(), ItemDragListener {
    private lateinit var binding: FragmentTemplateGroupListBinding
    private val viewModel: TemplateGroupListViewModel by viewModels()
    private lateinit var listAdapter: TemplateGroupListAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_template_group_list,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        listAdapter = TemplateGroupListAdapter(viewModel)
        binding.adapter = listAdapter
        itemTouchHelper = ItemTouchHelper(ItemDragHelperCallback(this)).apply {
            attachToRecyclerView(binding.templateGroupsRecyclerView)
        }
        observeUI()
    }

    private fun observeUI() {
        viewModel.groups.observe(viewLifecycleOwner) { list ->
            binding.tvListEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            listAdapter.submitList(list)
          //  listAdapter.notifyDataSetChanged()
        }

        viewModel.addGroupEvent.observe(viewLifecycleOwner) {
            showEditableGroupDialog(it)
        }
        viewModel.selectedGroupEvent.observe(viewLifecycleOwner) {
            openSelectedGroup(it)
        }
        viewModel.popupMenuEvent.observe(viewLifecycleOwner) {
            showPopup(it.first, it.second)
        }
        viewModel.startDragEvent.observe(viewLifecycleOwner){
            itemTouchHelper.startDrag(it)
        }
    }

    private fun openSelectedGroup(group: TemplateGroup) {
        navigate {
            TemplateGroupListFragmentDirections.actionOpenGroupTemplates(group.templateGroupId)
        }
    }

    private fun showEditableGroupDialog(group: TemplateGroup) {
        navigate {
            TemplateGroupListFragmentDirections.actionOpenEditableGroup(group)
        }
    }

    private fun showPopup(view: View, group: TemplateGroup) {
        ItemPopupMenu(requireContext(), view).showEditDelete { result ->
            when (result) {
                EDIT -> showEditableGroupDialog(group)
                DELETE -> deleteGroup(group)
            }
        }
    }

    private fun deleteGroup(item: TemplateGroup) {
        DeleteConfirmationAlertDialog(requireContext()).apply {
            title = getString(R.string.delete_group)
            message = getString(R.string.delete_group_confirmation)
            show { result ->
                if (result) viewModel.deleteGroup(item)
            }
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        listAdapter.moveItems(fromPosition, toPosition)
    }

    override fun onMoveFinished() {
        viewModel.updateAll(listAdapter.currentList)
    }
}