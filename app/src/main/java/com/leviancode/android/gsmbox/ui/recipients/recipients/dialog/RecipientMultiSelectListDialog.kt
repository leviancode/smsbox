package com.leviancode.android.gsmbox.ui.recipients.recipients.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.databinding.DialogSelectListBinding
import com.leviancode.android.gsmbox.ui.recipients.recipients.adapters.RecipientMultiSelectListAdapter
import com.leviancode.android.gsmbox.utils.REQ_MULTI_SELECT_RECIPIENT
import com.leviancode.android.gsmbox.utils.extensions.goBack
import com.leviancode.android.gsmbox.utils.extensions.setNavigationResult

class RecipientMultiSelectListDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogSelectListBinding
    private val viewModel: RecipientMultiSelectListViewModel by viewModels()
    private val args: RecipientMultiSelectListDialogArgs by navArgs()
    private lateinit var listAdapter: RecipientMultiSelectListAdapter

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
        binding.bootomSheetLayout.setPadding(0,0,0,8)
        binding.toolbar.title = getString(R.string.select_recipients)
        listAdapter = RecipientMultiSelectListAdapter(viewModel)
        binding.bottomSheetRecyclerView.adapter = listAdapter

        fetchData()
        observeEvents()
    }

    private fun observeEvents() {
        binding.btnOk.setOnClickListener {
            setSelectedAndQuit(viewModel.selectedItems)
        }
    }

    private fun fetchData() {
        viewModel.loadRecipientsAndSelectByGroupId(args.groupId).observe(viewLifecycleOwner) { list ->
            binding.tvListEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            listAdapter.recipients = list
        }
    }

    private fun setSelectedAndQuit(selectedRecipients: List<Recipient>) {
        setNavigationResult(selectedRecipients, REQ_MULTI_SELECT_RECIPIENT)
        goBack()
    }
}