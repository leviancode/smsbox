package com.leviancode.android.gsmbox.ui.recipients.view.dialog.bottomsheets

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
import com.leviancode.android.gsmbox.adapters.RecipientGroupMultiSelectListAdapter
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.databinding.ButtonNewGroupBinding
import com.leviancode.android.gsmbox.databinding.DialogSelectListBinding
import com.leviancode.android.gsmbox.ui.recipients.viewmodel.RecipientGroupMultiSelectListViewModel
import com.leviancode.android.gsmbox.utils.REQ_MULTI_SELECT_RECIPIENT_GROUP
import com.leviancode.android.gsmbox.utils.REQ_SELECT_RECIPIENT_GROUP
import com.leviancode.android.gsmbox.utils.extensions.*

class RecipientGroupMultiSelectListDialog : BottomSheetDialogFragment(){
    private lateinit var binding: DialogSelectListBinding
    private val viewModel: RecipientGroupMultiSelectListViewModel by viewModels()
    private val args: RecipientGroupMultiSelectListDialogArgs by navArgs()
    private lateinit var listAdapter: RecipientGroupMultiSelectListAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog is BottomSheetDialog) {
            dialog.setOnShowListener {
                val container = dialog.findViewById<FrameLayout>(R.id.container)
                val bind = ButtonNewGroupBinding.inflate(dialog.layoutInflater)
                container?.addView(bind.root)
                bind.btnNewGroup.setOnClickListener {
                    showNewGroupDialog()
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
        listAdapter = RecipientGroupMultiSelectListAdapter(viewModel)
        binding.bottomSheetRecyclerView.adapter = listAdapter
        binding.toolbar.title = getString(R.string.select_group)
        viewModel.loadGroups(args.recipientId).observe(viewLifecycleOwner){ list ->
            binding.tvListEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            listAdapter.groups = list
        }

        binding.btnOk.setOnClickListener {
            setSelectedAndExit()
        }
    }

    private fun showNewGroupDialog() {
        getNavigationResult<RecipientGroup>(REQ_SELECT_RECIPIENT_GROUP)?.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                viewModel.onItemClick(result)
                removeNavigationResult<Recipient>(REQ_SELECT_RECIPIENT_GROUP)
            }
        }

        navigate {
            RecipientGroupMultiSelectListDialogDirections.actionOpenEditableRecipientGroup(null)
        }
    }

    private fun setSelectedAndExit(){
        setNavigationResult(viewModel.selectedItems, REQ_MULTI_SELECT_RECIPIENT_GROUP)
        goBack()
    }
}