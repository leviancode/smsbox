package com.brainymobile.android.smsbox.ui.screens.recipients.groups.select

import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.brainymobile.android.smsbox.R
import com.brainymobile.android.smsbox.databinding.DialogSelectListBinding
import com.brainymobile.android.smsbox.databinding.MultiSelectListItemRecipientGroupBinding
import com.brainymobile.android.smsbox.databinding.ViewButtonNewGroupBinding
import com.brainymobile.android.smsbox.ui.base.BaseBottomSheet
import com.brainymobile.android.smsbox.ui.base.BaseListAdapter
import com.brainymobile.android.smsbox.ui.entities.recipients.RecipientGroupUI
import com.brainymobile.android.smsbox.ui.screens.recipients.groups.edit.RecipientGroupEditDialog
import com.brainymobile.android.smsbox.utils.extensions.observe
import com.brainymobile.android.smsbox.utils.extensions.scrollToEnd
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipientGroupMultiSelectListDialog private constructor(
    private val selectedGroupsIds: List<Int>,
    private val callback: (List<RecipientGroupUI>) -> Unit
) : BaseBottomSheet<DialogSelectListBinding>(R.layout.dialog_select_list) {
    private val viewModel: RecipientGroupMultiSelectListViewModel by viewModels()
    private val listAdapter =
        BaseListAdapter<RecipientGroupUI, MultiSelectListItemRecipientGroupBinding>(R.layout.multi_select_list_item_recipient_group) { binding, item, position ->
            binding.viewModel = viewModel
            binding.model = item
        }
    private var firstLoad = true

    override fun onCreated() {
        setupDialog()
    }

    private fun setupDialog() {
        binding.toolbar.title = getString(R.string.select_groups)
        requireDialog().setOnShowListener {
            val container = requireDialog().findViewById<FrameLayout>(R.id.container)
            val bind = ViewButtonNewGroupBinding.inflate(requireDialog().layoutInflater)
            container?.addView(bind.root)
            bind.btnNewGroup.setOnClickListener {
                openNewGroupDialog()
            }
            binding.btnOk.setOnClickListener {
                setSelectedAndExit(viewModel.selectedGroups)
            }
            binding.recyclerView.adapter = listAdapter
            observeData()
        }
    }

    private fun observeData() {
        viewModel.loadAndSelectGroupsByIds(selectedGroupsIds)
        viewModel.groupsFlow.observe(viewLifecycleOwner) { list ->
            binding.tvListEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            listAdapter.submitList(list) {
                if (!firstLoad) {
                    binding.recyclerView.scrollToEnd()
                }
                firstLoad = false
            }
        }
    }

    private fun openNewGroupDialog() {
        RecipientGroupEditDialog.show(childFragmentManager, onGroupEdit = { selectedGroupId ->
            viewModel.selectNewGroup(selectedGroupId)
        })
    }

    private fun setSelectedAndExit(selectedGroups: List<RecipientGroupUI>) {
        callback(selectedGroups)
        close()
    }

    companion object {
        fun show(
            fm: FragmentManager,
            selectedGroupsIds: List<Int>,
            callback: (List<RecipientGroupUI>) -> Unit
        ) {
            RecipientGroupMultiSelectListDialog(selectedGroupsIds, callback).show(
                fm,
                null
            )
        }
    }
}