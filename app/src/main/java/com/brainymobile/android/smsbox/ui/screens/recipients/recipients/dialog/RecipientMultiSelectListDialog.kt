package com.brainymobile.android.smsbox.ui.screens.recipients.recipients.dialog

import android.view.View
import androidx.fragment.app.FragmentManager
import com.brainymobile.android.smsbox.R
import com.brainymobile.android.smsbox.databinding.DialogSelectListBinding
import com.brainymobile.android.smsbox.databinding.MultiSelectListItemRecipientBinding
import com.brainymobile.android.smsbox.ui.base.BaseBottomSheet
import com.brainymobile.android.smsbox.ui.base.BaseListAdapter
import com.brainymobile.android.smsbox.ui.entities.recipients.RecipientUI
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipientMultiSelectListDialog(
    private val groupId: Int,
    private val callback: (List<RecipientUI>) -> Unit
) : BaseBottomSheet<DialogSelectListBinding>(R.layout.dialog_select_list) {
    private val viewModel: RecipientMultiSelectListViewModel by viewModel()
    private val listAdapter =
        BaseListAdapter<RecipientUI, MultiSelectListItemRecipientBinding>(R.layout.multi_select_list_item_recipient) { binding, item, position ->
            binding.viewModel = viewModel
            binding.model = item
        }

    override fun onCreated() {
        binding.apply {
            bootomSheetLayout.setPadding(0, 0, 0, 8)
            toolbar.title = getString(R.string.select_recipients)
            recyclerView.adapter = listAdapter
        }

        observeData()
        observeEvents()
    }

    private fun observeEvents() {
        binding.btnOk.setOnClickListener {
            setSelectedAndQuit(viewModel.selectedItems)
        }
    }

    private fun observeData() {
        viewModel.loadRecipientsAndSelectByGroupId(groupId)
            .observe(viewLifecycleOwner) { list ->
                binding.tvListEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                listAdapter.submitList(list)
            }
    }

    private fun setSelectedAndQuit(selectedRecipients: List<RecipientUI>) {
        callback(selectedRecipients)
        close()
    }

    companion object {
        fun show(fm: FragmentManager, groupId: Int, callback: (List<RecipientUI>) -> Unit){
            RecipientMultiSelectListDialog(groupId, callback).show(fm, null)
        }
    }
}