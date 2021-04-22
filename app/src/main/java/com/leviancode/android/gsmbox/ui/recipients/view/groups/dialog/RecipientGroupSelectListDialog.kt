package com.leviancode.android.gsmbox.ui.recipients.view.groups.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.ui.recipients.adapters.RecipientGroupSelectListAdapter
import com.leviancode.android.gsmbox.databinding.DialogSelectListBinding
import com.leviancode.android.gsmbox.utils.REQ_SELECT_RECIPIENT_GROUP
import com.leviancode.android.gsmbox.utils.extensions.goBack
import com.leviancode.android.gsmbox.utils.extensions.setNavigationResult

class RecipientGroupSelectListDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogSelectListBinding
    private val viewModel: RecipientGroupSelectListViewModel by viewModels()
    private val args: RecipientGroupSelectListDialogArgs by navArgs()
    private lateinit var listAdapter: RecipientGroupSelectListAdapter

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

        viewModel.loadNotEmptyGroupsAndSelectByGroupId(args.groupId).observe(viewLifecycleOwner){ list ->
            binding.tvListEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            listAdapter.groups = list
        }

        binding.btnOk.setOnClickListener {
            setSelectedAndExit(viewModel.getSelectedGroupId())
        }
    }

    private fun setSelectedAndExit(selectedGroupId: Int) {
        setNavigationResult(selectedGroupId, REQ_SELECT_RECIPIENT_GROUP)
        goBack()
    }
}