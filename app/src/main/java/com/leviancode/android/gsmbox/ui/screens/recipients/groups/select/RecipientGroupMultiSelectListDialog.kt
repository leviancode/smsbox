package com.leviancode.android.gsmbox.ui.screens.recipients.groups.select

import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.DialogSelectListBinding
import com.leviancode.android.gsmbox.databinding.MultiSelectListItemRecipientGroupBinding
import com.leviancode.android.gsmbox.databinding.ViewButtonNewGroupBinding
import com.leviancode.android.gsmbox.ui.base.BaseBottomSheet
import com.leviancode.android.gsmbox.ui.base.BaseListAdapter
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientGroupUI
import com.leviancode.android.gsmbox.ui.screens.recipients.groups.edit.RecipientGroupEditDialog
import com.leviancode.android.gsmbox.utils.extensions.observe
import com.leviancode.android.gsmbox.utils.extensions.scrollToEnd
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipientGroupMultiSelectListDialog private constructor(
    private val selectedGroupsIds: List<Int>,
    private val callback: (List<RecipientGroupUI>) -> Unit
) : BaseBottomSheet<DialogSelectListBinding>(R.layout.dialog_select_list) {
    private val viewModel: RecipientGroupMultiSelectListViewModel by viewModel()
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
            listAdapter.submitList(list){
                if (!firstLoad) {
                    binding.recyclerView.scrollToEnd()
                }
                firstLoad = false
            }
        }
    }

    private fun openNewGroupDialog() {
        RecipientGroupEditDialog.show(childFragmentManager) { selectedGroupId ->
            viewModel.selectNewGroup(selectedGroupId)
        }
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