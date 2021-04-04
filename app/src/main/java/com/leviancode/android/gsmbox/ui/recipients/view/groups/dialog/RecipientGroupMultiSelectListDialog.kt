package com.leviancode.android.gsmbox.ui.recipients.view.groups.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.ui.recipients.adapters.RecipientGroupSelectListAdapter
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.databinding.ButtonNewGroupBinding
import com.leviancode.android.gsmbox.databinding.DialogSelectListBinding
import com.leviancode.android.gsmbox.utils.REQ_MULTI_SELECT_RECIPIENT_GROUP
import com.leviancode.android.gsmbox.utils.REQ_SELECT_RECIPIENT_GROUP
import com.leviancode.android.gsmbox.utils.extensions.*

class RecipientGroupMultiSelectListDialog : BottomSheetDialogFragment(){
    private lateinit var binding: DialogSelectListBinding
    private val viewModel: RecipientGroupSelectListViewModel by viewModels()
    private val args: RecipientGroupMultiSelectListDialogArgs by navArgs()
    private lateinit var listAdapter: RecipientGroupSelectListAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog is BottomSheetDialog) {
            dialog.setOnShowListener {
                val container = dialog.findViewById<FrameLayout>(R.id.container)
                val bind = ButtonNewGroupBinding.inflate(dialog.layoutInflater)
                container?.addView(bind.root)
                bind.btnNewGroup.setOnClickListener {
                    openNewGroupDialog()
                }
            }
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.dialog_select_list, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listAdapter = RecipientGroupSelectListAdapter(viewModel)
        binding.bottomSheetRecyclerView.adapter = listAdapter
        binding.toolbar.title = getString(R.string.select_groups)
        viewModel.multiSelectMode = true

        viewModel.loadAllGroupsAndSelectByRecipientId(args.recipientId).observe(viewLifecycleOwner){ list ->
            binding.tvListEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            listAdapter.groups = list
            if (!viewModel.firstLoad){
                binding.bottomSheetRecyclerView.layoutManager?.scrollToPosition(listAdapter.itemCount - 1)
            }
            viewModel.firstLoad = false
        }

        binding.btnOk.setOnClickListener {
            setSelectedAndExit()
        }
    }

    private fun openNewGroupDialog() {
        getNavigationResult<RecipientGroup>(REQ_SELECT_RECIPIENT_GROUP)?.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                viewModel.onItemClick(result)
                removeNavigationResult<RecipientGroup>(REQ_SELECT_RECIPIENT_GROUP)
            }
        }

        navigate {
            RecipientGroupMultiSelectListDialogDirections.actionOpenEditableRecipientGroup(viewModel.getNewGroup())
        }
    }

    private fun setSelectedAndExit(){
        setNavigationResult(viewModel.selectedItems, REQ_MULTI_SELECT_RECIPIENT_GROUP)
        goBack()
    }
}