package com.brainymobile.android.smsbox.ui.screens.recipients.groups.select

import android.view.View
import androidx.fragment.app.FragmentManager
import com.brainymobile.android.smsbox.R
import com.brainymobile.android.smsbox.databinding.DialogSelectListBinding
import com.brainymobile.android.smsbox.databinding.SelectListItemRecipientGroupBinding
import com.brainymobile.android.smsbox.ui.base.BaseBottomSheet
import com.brainymobile.android.smsbox.ui.base.BaseListAdapter
import com.brainymobile.android.smsbox.ui.entities.recipients.RecipientGroupUI
import com.brainymobile.android.smsbox.utils.extensions.observe
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