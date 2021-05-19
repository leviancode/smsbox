package com.leviancode.android.gsmbox.ui.fragments.recipients.groups.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.DialogSelectListBinding
import com.leviancode.android.gsmbox.ui.fragments.recipients.groups.adapters.RecipientGroupSelectListAdapter
import com.leviancode.android.gsmbox.core.utils.REQ_SELECT_RECIPIENT_GROUP
import com.leviancode.android.gsmbox.core.utils.extensions.goBack
import com.leviancode.android.gsmbox.core.utils.extensions.setNavigationResult

class RecipientGroupSelectListDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogSelectListBinding
    private val viewModel: RecipientGroupSelectListViewModel by viewModels()
    private val args: RecipientGroupSelectListDialogArgs by navArgs()
    private lateinit var listAdapter: RecipientGroupSelectListAdapter

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
        binding.bootomSheetLayout.setPadding(0,0,0,8)
        requireDialog().setOnShowListener {
            listAdapter = RecipientGroupSelectListAdapter(viewModel)
            binding.bottomSheetRecyclerView.adapter = listAdapter
            fetchData()
        }
        binding.btnOk.setOnClickListener {
            setSelectedAndExit(viewModel.selectGroupId)
        }
    }

    private fun fetchData() {
        viewModel.selectGroup(args.groupId)
        viewModel.getGroups().observe(viewLifecycleOwner) { list ->
                binding.tvListEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                listAdapter.groups = list
            }
    }

    private fun setSelectedAndExit(selectedGroupId: Int) {
        setNavigationResult(selectedGroupId, REQ_SELECT_RECIPIENT_GROUP)
        goBack()
    }
}