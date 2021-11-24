package com.leviancode.android.gsmbox.ui.screens.recipients.groups.select

import android.view.View
import android.widget.FrameLayout
import androidx.navigation.fragment.navArgs
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.DialogSelectListBinding
import com.leviancode.android.gsmbox.databinding.MultiSelectListItemRecipientGroupBinding
import com.leviancode.android.gsmbox.databinding.ViewButtonNewGroupBinding
import com.leviancode.android.gsmbox.ui.base.BaseBottomSheet
import com.leviancode.android.gsmbox.ui.base.GenericListAdapter
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientGroupUI
import com.leviancode.android.gsmbox.utils.REQ_CREATE_RECIPIENT_GROUP
import com.leviancode.android.gsmbox.utils.REQ_MULTI_SELECT_RECIPIENT_GROUP
import com.leviancode.android.gsmbox.utils.extensions.getNavigationResult
import com.leviancode.android.gsmbox.utils.extensions.navigate
import com.leviancode.android.gsmbox.utils.extensions.removeNavigationResult
import com.leviancode.android.gsmbox.utils.extensions.setNavigationResult
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipientGroupMultiSelectListDialog :
    BaseBottomSheet<DialogSelectListBinding>(R.layout.dialog_select_list) {
    private val viewModel: RecipientGroupMultiSelectListViewModel by viewModel()
    private val args: RecipientGroupMultiSelectListDialogArgs by navArgs()
    private val listAdapter =
        GenericListAdapter<RecipientGroupUI, MultiSelectListItemRecipientGroupBinding>(R.layout.multi_select_list_item_recipient_group) { item, binding ->
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
                setSelectedAndExit()
            }
            binding.recyclerView.adapter = listAdapter
            observeData()
        }
    }

    private fun observeData() {
        viewModel.selectGroups(args.groupIds)
        viewModel.groups.observe(viewLifecycleOwner) { list ->
            binding.tvListEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            listAdapter.submitList(list)
            if (!firstLoad) {
                binding.recyclerView.layoutManager?.scrollToPosition(listAdapter.itemCount - 1)
            }
            firstLoad = false
        }
    }

    private fun openNewGroupDialog() {
        getNavigationResult<RecipientGroupUI>(REQ_CREATE_RECIPIENT_GROUP)?.observe(
            viewLifecycleOwner
        ) { result ->
            if (result != null) {
                viewModel.selectNewGroup(result)
            }
            removeNavigationResult<RecipientGroupUI>(REQ_CREATE_RECIPIENT_GROUP)
        }

        navigate {
            RecipientGroupMultiSelectListDialogDirections.actionOpenEditableRecipientGroup()
        }
    }

    private fun setSelectedAndExit() {
        setNavigationResult(viewModel.getSelectedGroups(), REQ_MULTI_SELECT_RECIPIENT_GROUP)
        close()
    }
}