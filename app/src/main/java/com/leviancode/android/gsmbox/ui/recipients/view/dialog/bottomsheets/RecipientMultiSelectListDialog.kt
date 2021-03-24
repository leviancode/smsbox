package com.leviancode.android.gsmbox.ui.recipients.view.dialog.bottomsheets

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.RecipientSelectListAdapter
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.databinding.ButtonContactsBinding
import com.leviancode.android.gsmbox.databinding.DialogSelectListBinding
import com.leviancode.android.gsmbox.ui.recipients.viewmodel.RecipientSelectListViewModel
import com.leviancode.android.gsmbox.utils.REQ_MULTI_SELECT_RECIPIENT
import com.leviancode.android.gsmbox.utils.extensions.goBack
import com.leviancode.android.gsmbox.utils.extensions.setNavigationResult
import com.leviancode.android.gsmbox.utils.managers.ContactsManager

class RecipientMultiSelectListDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogSelectListBinding
    private val viewModel: RecipientSelectListViewModel by viewModels()
    private val args: RecipientMultiSelectListDialogArgs by navArgs()
    private lateinit var listAdapter: RecipientSelectListAdapter
    private lateinit var contactsLauncher: ActivityResultLauncher<Void>

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
        binding.bootomSheetLayout.setPadding(0,0,0,8)
        contactsLauncher =
            registerForActivityResult(PickContact()) { uri ->
                viewModel.selectRecipientByContactUri(requireContext(), uri)
                setSelectedAndQuit(viewModel.selectedItems)
            }
        binding.toolbar.title = getString(R.string.select_recipients)
        viewModel.multiSelectMode = true
        listAdapter = RecipientSelectListAdapter(viewModel)
        binding.bottomSheetRecyclerView.adapter = listAdapter

        observeUI()
    }

    private fun observeUI() {
        viewModel.loadRecipientsAndSelectByGroupId(args.groupId).observe(viewLifecycleOwner) { list ->
            binding.tvListEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            listAdapter.recipients = list
        }

        binding.btnOk.setOnClickListener {
            setSelectedAndQuit(viewModel.selectedItems)
        }
    }

    private fun selectContact() {
        ContactsManager.openContactsApp(requireContext(), contactsLauncher)
    }

    private fun setSelectedAndQuit(selectedRecipients: List<Recipient>) {
        setNavigationResult(selectedRecipients, REQ_MULTI_SELECT_RECIPIENT)
        goBack()
    }
}