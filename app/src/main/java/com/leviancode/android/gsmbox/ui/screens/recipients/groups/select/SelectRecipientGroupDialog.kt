package com.leviancode.android.gsmbox.ui.screens.recipients.groups.select

import android.view.View
import androidx.fragment.app.FragmentManager
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.DialogSelectListBinding
import com.leviancode.android.gsmbox.databinding.SelectListItemRecipientGroupBinding
import com.leviancode.android.gsmbox.ui.base.BaseBottomSheet
import com.leviancode.android.gsmbox.ui.base.BaseListAdapter
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientGroupUI
import com.leviancode.android.gsmbox.utils.extensions.observe
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectRecipientGroupDialog private constructor(
    private val currentGroupId: Int = 0,
    private val callback: (Int) -> Unit
) :
    BaseBottomSheet<DialogSelectListBinding>(R.layout.dialog_select_list) {
    private val viewModel: RecipientGroupSelectListViewModel by viewModel()
    private val listAdapter =
        BaseListAdapter<RecipientGroupUI, SelectListItemRecipientGroupBinding>(R.layout.select_list_item_recipient_group) { binding, item, position ->
            binding.viewModel = viewModel
            binding.model = item
        }

    override fun onCreated() {
        binding.toolbar.title = getString(R.string.select_group)
        binding.bootomSheetLayout.setPadding(0, 0, 0, 8)
        requireDialog().setOnShowListener {
            binding.recyclerView.adapter = listAdapter
            observeData()
        }
        viewModel.selectedGroup.observe(viewLifecycleOwner) {
            setSelectedAndExit(it)
        }
    }

    private fun observeData() {
        viewModel.getGroups(currentGroupId).observe(viewLifecycleOwner) { list ->
            binding.tvListEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            listAdapter.submitList(list)
        }
    }

    private fun setSelectedAndExit(selectedGroupId: Int) {
        callback(selectedGroupId)
        close()
    }

    companion object {
        fun show(fm: FragmentManager, currentGroupId: Int, callback: (Int) -> Unit) {
            SelectRecipientGroupDialog(currentGroupId, callback).show(fm, null)
        }
    }
}