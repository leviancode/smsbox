package com.leviancode.android.gsmbox.ui.fragments.templates.groups.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView

import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.core.data.model.templates.GroupWithTemplates
import com.leviancode.android.gsmbox.ui.fragments.templates.groups.adapters.TemplateGroupListAdapter
import com.leviancode.android.gsmbox.core.data.model.templates.TemplateGroup
import com.leviancode.android.gsmbox.databinding.FragmentTemplateGroupListBinding
import com.leviancode.android.gsmbox.core.utils.helpers.ItemDragListener
import com.leviancode.android.gsmbox.ui.dialogs.alertdialogs.DeleteConfirmationAlertDialog
import com.leviancode.android.gsmbox.ui.dialogs.ItemPopupMenu
import com.leviancode.android.gsmbox.ui.dialogs.ItemPopupMenu.Companion.DELETE
import com.leviancode.android.gsmbox.ui.dialogs.ItemPopupMenu.Companion.EDIT
import com.leviancode.android.gsmbox.core.utils.extensions.navigate

class TemplateGroupListFragment : Fragment(), ItemDragListener {
    private lateinit var binding: FragmentTemplateGroupListBinding
    private val viewModel: TemplateGroupListViewModel by activityViewModels()
    private lateinit var listAdapter: TemplateGroupListAdapter
  //  private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTemplateGroupListBinding.inflate(
            inflater,
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
       /* itemTouchHelper = ItemTouchHelper(ItemDragHelperCallback(this)).apply {
            attachToRecyclerView(binding.templateGroupsRecyclerView)
        }*/
        fetchData()
        observeEvents()
    }

    private fun observeEvents() {
        binding.templateGroupsRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            private val fab = binding.fabAddGroup
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && fab.isShown) fab.hide()
                else if (dy < 0 && !fab.isShown) fab.show()
            }
        })

        viewModel.addGroupEvent.observe(viewLifecycleOwner) {
            showEditableGroupDialog(it)
        }
        viewModel.selectedGroupEvent.observe(viewLifecycleOwner) {
            openSelectedGroup(it)
        }
        viewModel.popupMenuEvent.observe(viewLifecycleOwner) {
            showPopup(it.first, it.second)
        }
        /*viewModel.startDragEvent.observe(viewLifecycleOwner) {
            itemTouchHelper.startDrag(it)
        }*/
    }

    private fun fetchData() {
        viewModel.getGroupsWithTemplates().observe(viewLifecycleOwner) { list ->
            binding.tvListEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            listAdapter.submitList(list)
        }
    }

    private fun openSelectedGroup(group: TemplateGroup) {
        navigate {
            TemplateGroupListFragmentDirections.actionOpenGroupTemplates(
                group.templateGroupId, group.getName()
            )
        }
    }

    private fun showEditableGroupDialog(groupId: Int) {
        navigate {
            TemplateGroupListFragmentDirections.actionOpenEditableGroup(groupId)
        }
    }

    private fun showPopup(view: View, group: GroupWithTemplates) {
        ItemPopupMenu(requireContext(), view).showEditDelete { result ->
            when (result) {
                EDIT -> showEditableGroupDialog(group.group.templateGroupId)
                DELETE -> deleteGroup(group)
            }
        }
    }

    private fun deleteGroup(item: GroupWithTemplates) {
        DeleteConfirmationAlertDialog.show(
            requireContext(),
            getString(R.string.delete_group),
            getString(R.string.delete_group_confirmation)
        ) { confirmed ->
            if (confirmed) {
                viewModel.deleteGroup(item)
                if (!binding.fabAddGroup.isShown) binding.fabAddGroup.show()
            }
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Pair<Int, Int> {
        return listAdapter.moveItems(fromPosition, toPosition)
    }

    override fun onMoveFinished(firstId: Int, secondId: Int) {
        viewModel.replaceGroupsPosition(firstId, secondId)
    }
}