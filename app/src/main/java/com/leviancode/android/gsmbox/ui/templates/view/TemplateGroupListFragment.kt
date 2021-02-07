package com.leviancode.android.gsmbox.ui.templates.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.customview.widget.ViewDragHelper
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.TemplateGroupListAdapter
import com.leviancode.android.gsmbox.data.model.TemplateGroup
import com.leviancode.android.gsmbox.data.model.TemplateGroupObservable
import com.leviancode.android.gsmbox.databinding.FragmentTemplateGroupListBinding
import com.leviancode.android.gsmbox.ui.extra.DeleteConfirmationDialog
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu
import com.leviancode.android.gsmbox.ui.templates.viewmodel.TemplateGroupListViewModel
import com.leviancode.android.gsmbox.utils.DELETE
import com.leviancode.android.gsmbox.utils.EDIT

class TemplateGroupListFragment : Fragment() {
    private lateinit var binding: FragmentTemplateGroupListBinding
    private val viewModel: TemplateGroupListViewModel by viewModels()
    private lateinit var navController: NavController

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
        navController = view.findNavController()
        binding.viewModel = viewModel
        binding.adapter = TemplateGroupListAdapter(viewModel)

        binding.templateGroupsRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) {
                    binding.fabAddGroup.show()
                } else if (dy > 0) {
                    binding.fabAddGroup.hide()
                }
            }
        })

        observeUI()
    }

    private fun openSelectedGroup(group: TemplateGroup) {
        val action = TemplateGroupListFragmentDirections.actionOpenGroupTemplates(
            group.groupId,
            group.name
        )

        navController.navigate(action)
    }

    private fun showEditableGroupDialog(groupId: String?) {
        val action = TemplateGroupListFragmentDirections.actionOpenEditableGroup(groupId)
        navController.navigate(action)
    }

    private fun observeUI() {
        viewModel.groups.observe(viewLifecycleOwner) { groupList ->
            binding.adapter?.submitList(groupList)
        }

        viewModel.addGroupLiveEvent.observe(viewLifecycleOwner) { showEditableGroupDialog(null) }
        viewModel.selectGroupLiveEvent.observe(viewLifecycleOwner) { openSelectedGroup(it) }

        viewModel.popupMenuLiveEvent.observe(viewLifecycleOwner) {
            showPopup(it)
        }
    }

    private fun showPopup(pair: Pair<View, TemplateGroup>) {
        ItemPopupMenu(requireContext(), pair.first).show { result ->
            when (result) {
                EDIT -> showEditableGroupDialog(pair.second.groupId)
                DELETE -> deleteGroup(pair.second)
            }
        }
    }

    private fun deleteGroup(item: TemplateGroup) {
        DeleteConfirmationDialog(requireContext()).apply {
            title = getString(R.string.delete_group)
            message = getString(R.string.delete_group_confirmation)
            show{ result ->
                if (result) viewModel.deleteGroup(item)
            }
        }
    }

    private inner class ViewDragHelperCallback : ViewDragHelper.Callback() {

        override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
            if (capturedChild is MaterialCardView) {
                (view as MaterialCardView).isDragged = true
            }
        }

        override fun onViewReleased(releaseChild: View, xVel: Float, yVel: Float) {
            if (releaseChild is MaterialCardView) {
                (view as MaterialCardView).isDragged = false
            }
        }

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            TODO("Not yet implemented")
        }
    }
}