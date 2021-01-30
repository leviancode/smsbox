package com.leviancode.android.gsmbox.ui.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.customview.widget.ViewDragHelper
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.card.MaterialCardView

import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.ListItemClickListener
import com.leviancode.android.gsmbox.adapters.TemplateGroupListAdapter
import com.leviancode.android.gsmbox.data.model.TemplateGroup
import com.leviancode.android.gsmbox.databinding.FragmentTemplateGroupListBinding
import com.leviancode.android.gsmbox.ui.view.dialog.NewGroupDialog
import com.leviancode.android.gsmbox.ui.viewmodel.TemplatesViewModel
import com.leviancode.android.gsmbox.utils.ARG_GROUP_ID

class TemplateGroupListFragment : Fragment() {
    private lateinit var binding: FragmentTemplateGroupListBinding
    private val viewModel: TemplatesViewModel by activityViewModels()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_template_group_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()
        binding.adapter = TemplateGroupListAdapter().apply {
            clickListener = ListItemClickListener { openSelectedGroup(it) }
        }

        binding.fabAddGroup.setOnClickListener{
            showNewGroupDialog()
        }

        observeUI()
    }

    private fun openSelectedGroup(group: TemplateGroup) {
        val action = TemplateGroupListFragmentDirections
            .actionTemplateGroupsToTemplates(group.id, group.name)
        
        navController.navigate(action)
    }

    private fun showNewGroupDialog(){
        val action = TemplateGroupListFragmentDirections
            .actionTemplateGroupsToNewGroup()
        navController.navigate(action)
    }

    private fun observeUI(){
        viewModel.groups.observe(viewLifecycleOwner){ groupList ->
            binding.adapter?.submitList(groupList)
        }

        /*viewModel.selectedGroup.observe(viewLifecycleOwner){ group ->
            openSelectedGroup(group)
        }*/
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