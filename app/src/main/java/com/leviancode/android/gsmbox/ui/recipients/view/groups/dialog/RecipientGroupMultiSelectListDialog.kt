package com.leviancode.android.gsmbox.ui.recipients.view.groups.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.ButtonNewGroupBinding
import com.leviancode.android.gsmbox.databinding.DialogSelectListBinding
import com.leviancode.android.gsmbox.ui.recipients.adapters.RecipientGroupMultiSelectListAdapter
import com.leviancode.android.gsmbox.utils.REQ_CREATE_RECIPIENT_GROUP
import com.leviancode.android.gsmbox.utils.REQ_MULTI_SELECT_RECIPIENT_GROUP
import com.leviancode.android.gsmbox.utils.extensions.*
import com.leviancode.android.gsmbox.utils.log

class RecipientGroupMultiSelectListDialog : BottomSheetDialogFragment(){
    private lateinit var binding: DialogSelectListBinding
    private val viewModel: RecipientGroupMultiSelectListViewModel by viewModels()
    private val args: RecipientGroupMultiSelectListDialogArgs by navArgs()
    private lateinit var listAdapter: RecipientGroupMultiSelectListAdapter
    private var firstLoad = true

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog is BottomSheetDialog) {
            /*dialog.setOnShowListener {
                val container = dialog.findViewById<FrameLayout>(R.id.container)
                val bind = ButtonNewGroupBinding.inflate(dialog.layoutInflater)
                container?.addView(bind.root)
                bind.btnNewGroup.setOnClickListener {
                    openNewGroupDialog()
                }
            }*/
        }
        return dialog
    }

    override fun onHiddenChanged(hidden: Boolean) {
        log("hidden: $hidden")
        if (hidden) dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSelectListBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    private fun setupDialog(){
        binding.toolbar.title = getString(R.string.select_groups)
        requireDialog().setOnShowListener {
            val container = requireDialog().findViewById<FrameLayout>(R.id.container)
            val bind = ButtonNewGroupBinding.inflate(requireDialog().layoutInflater)
            container?.addView(bind.root)
            bind.btnNewGroup.setOnClickListener {
                openNewGroupDialog()
            }
            binding.btnOk.setOnClickListener {
                setSelectedAndExit()
            }
            listAdapter = RecipientGroupMultiSelectListAdapter(viewModel)
            binding.bottomSheetRecyclerView.adapter = listAdapter
            fetchData()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDialog()
    }

    private fun fetchData() {
        viewModel.selectGroups(args.groupIds)
        viewModel.getGroups().observe(viewLifecycleOwner) { list ->
            binding.tvListEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            listAdapter.groups = list
            if (!firstLoad) {
                binding.bottomSheetRecyclerView.layoutManager?.scrollToPosition(listAdapter.itemCount - 1)
            }
            firstLoad = false
        }
    }

    private fun openNewGroupDialog() {
        getNavigationResult<Int>(REQ_CREATE_RECIPIENT_GROUP)?.observe(viewLifecycleOwner) { result ->
            if (result != 0) {
                viewModel.selectNewGroup(result)
            }
            removeNavigationResult<Int>(REQ_CREATE_RECIPIENT_GROUP)
        }

        navigate {
            RecipientGroupMultiSelectListDialogDirections.actionOpenEditableRecipientGroup()
        }
    }

    private fun setSelectedAndExit(){
        setNavigationResult(viewModel.getSelectedGroupIds(), REQ_MULTI_SELECT_RECIPIENT_GROUP)
        goBack()
    }
}