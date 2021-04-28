package com.leviancode.android.gsmbox.ui.recipients.groups.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.DialogSelectListBinding
import com.leviancode.android.gsmbox.databinding.ViewButtonNewGroupBinding
import com.leviancode.android.gsmbox.ui.recipients.groups.adapters.RecipientGroupMultiSelectListAdapter
import com.leviancode.android.gsmbox.utils.REQ_CREATE_RECIPIENT_GROUP
import com.leviancode.android.gsmbox.utils.REQ_MULTI_SELECT_RECIPIENT_GROUP
import com.leviancode.android.gsmbox.utils.extensions.*

class RecipientGroupMultiSelectListDialog : BottomSheetDialogFragment(){
    private lateinit var binding: DialogSelectListBinding
    private val viewModel: RecipientGroupMultiSelectListViewModel by viewModels()
    private val args: RecipientGroupMultiSelectListDialogArgs by navArgs()
    private lateinit var listAdapter: RecipientGroupMultiSelectListAdapter
    private var firstLoad = true

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDialog()
    }

    private fun setupDialog(){
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
            listAdapter = RecipientGroupMultiSelectListAdapter(viewModel)
            binding.bottomSheetRecyclerView.adapter = listAdapter
            fetchData()
        }
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