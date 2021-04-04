package com.leviancode.android.gsmbox.ui.recipients.view.recipients.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.ui.recipients.adapters.RecipientSelectListAdapter
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.databinding.ButtonContactsBinding
import com.leviancode.android.gsmbox.databinding.DialogSelectListBinding
import com.leviancode.android.gsmbox.utils.REQ_SELECT_RECIPIENT
import com.leviancode.android.gsmbox.utils.extensions.goBack
import com.leviancode.android.gsmbox.utils.extensions.setNavigationResult
import com.leviancode.android.gsmbox.utils.managers.ContactsManager

class RecipientSelectListDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogSelectListBinding
    private val viewModel: RecipientSelectListViewModel by viewModels()
    private val args: RecipientSelectListDialogArgs by navArgs()
    private lateinit var listAdapter: RecipientSelectListAdapter
    private lateinit var contactsLauncher: ActivityResultLauncher<Void>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog is BottomSheetDialog) {
            dialog.setOnShowListener {
                val container = dialog.findViewById<FrameLayout>(R.id.container)
                val bind = ButtonContactsBinding.inflate(dialog.layoutInflater)
                container?.addView(bind.root)
                bind.btnContacts.setOnClickListener {
                    selectContact()
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
        contactsLauncher =
            registerForActivityResult(ActivityResultContracts.PickContact()) { uri ->
                viewModel.selectRecipientByContactUri(requireContext(), uri)
                setSelectedAndQuit(viewModel.getSingleSelectedRecipient())
            }
        binding.toolbar.title = getString(R.string.select_recipient)
        listAdapter = RecipientSelectListAdapter(viewModel)
        binding.bottomSheetRecyclerView.adapter = listAdapter

        observeUI()
    }

    private fun observeUI() {
        viewModel.loadRecipientsAndSelectByPhoneNumber(args.phoneNumber).observe(viewLifecycleOwner) { list ->
            binding.tvListEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            listAdapter.recipients = list
        }

        binding.btnOk.setOnClickListener {
            setSelectedAndQuit(viewModel.getSingleSelectedRecipient())
        }
    }

    private fun selectContact() {
        ContactsManager.openContactsApp(requireContext(), contactsLauncher)
    }

    private fun setSelectedAndQuit(selectedRecipient: Recipient?) {
        setNavigationResult(selectedRecipient, REQ_SELECT_RECIPIENT)
        goBack()
    }
}




