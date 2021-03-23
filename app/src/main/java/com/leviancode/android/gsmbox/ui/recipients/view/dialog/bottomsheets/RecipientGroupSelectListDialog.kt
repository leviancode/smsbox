package com.leviancode.android.gsmbox.ui.recipients.view.dialog.bottomsheets

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.RecipientGroupSelectListAdapter
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.databinding.DialogSelectListBinding
import com.leviancode.android.gsmbox.ui.recipients.viewmodel.RecipientGroupSelectListViewModel
import com.leviancode.android.gsmbox.utils.REQ_SELECT_RECIPIENT_GROUP
import com.leviancode.android.gsmbox.utils.extensions.goBack
import com.leviancode.android.gsmbox.utils.extensions.setNavigationResult

class RecipientGroupSelectListDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogSelectListBinding
    private val viewModel: RecipientGroupSelectListViewModel by viewModels()
    private val args: RecipientGroupSelectListDialogArgs by navArgs()
    private lateinit var listAdapter: RecipientGroupSelectListAdapter
    private var selectedGroupId: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog is BottomSheetDialog) {
            dialog.behavior.skipCollapsed = true
            dialog.behavior.state = BottomSheetBehavior.STATE_DRAGGING
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
        binding.bootomSheetLayout.setPadding(0,0,0,8)
        binding.bottomSheetRecyclerView.adapter = listAdapter
        binding.toolbar.title = getString(R.string.select_group)

        viewModel.notEmptyGroups.observe(viewLifecycleOwner){ list ->
            if (list.isEmpty()){
                binding.tvListEmpty.visibility = View.VISIBLE
                binding.btnOk.isEnabled = true
            } else {
                binding.tvListEmpty.visibility = View.GONE
                list.find { it.group.recipientGroupId == args.groupId }?.let {
                    viewModel.onItemClick(it.group)
                }
            }
            listAdapter.groups = list
        }

        viewModel.selectedItem.observe(viewLifecycleOwner){
            binding.btnOk.isEnabled = true
            selectedGroupId = it
        }

        binding.btnOk.setOnClickListener {
            setSelectedAndExit()
        }
    }

    private fun setSelectedAndExit(){
        setNavigationResult(selectedGroupId, REQ_SELECT_RECIPIENT_GROUP)
        goBack()
    }
}