package com.leviancode.android.gsmbox.ui.recipients.recipients.dialog

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.DialogSelectListBinding
import com.leviancode.android.gsmbox.databinding.ViewButtonContactsBinding
import com.leviancode.android.gsmbox.ui.recipients.recipients.adapters.RecipientSelectListAdapter
import com.leviancode.android.gsmbox.utils.REQ_SELECT_RECIPIENT
import com.leviancode.android.gsmbox.utils.extensions.askPermission
import com.leviancode.android.gsmbox.utils.extensions.goBack
import com.leviancode.android.gsmbox.utils.extensions.setNavigationResult
import com.leviancode.android.gsmbox.utils.extensions.showToast
import com.leviancode.android.gsmbox.utils.log
import com.leviancode.android.gsmbox.utils.managers.ContactsManager

class RecipientSelectListDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogSelectListBinding
    private val viewModel: RecipientSelectListViewModel by viewModels()
    private val args: RecipientSelectListDialogArgs by navArgs()
    private lateinit var listAdapter: RecipientSelectListAdapter
    private lateinit var contactsLauncher: ActivityResultLauncher<Void>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSelectListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactsLauncher =
            registerForActivityResult(ActivityResultContracts.PickContact()) { uri ->
                viewModel.selectRecipientByContactUri(requireContext(), uri)
                setSelected(viewModel.selectedPhoneNumber)
            }
        binding.toolbar.title = getString(R.string.select_recipient)
        observeEvents()
    }

    private fun observeEvents() {
        requireDialog().setOnShowListener {
            listAdapter = RecipientSelectListAdapter(viewModel)
            binding.bottomSheetRecyclerView.adapter = listAdapter

            val container = requireDialog().findViewById<FrameLayout>(R.id.container)
            val bind = ViewButtonContactsBinding.inflate(requireDialog().layoutInflater)
            container?.addView(bind.root)
            bind.btnContacts.setOnClickListener {
                selectContact()
            }
            fetchData()
        }
        requireDialog().setOnDismissListener {
            setSelected(null)
        }
        binding.btnOk.setOnClickListener {
            setSelected(viewModel.selectedPhoneNumber)
        }

    }

    private fun fetchData() {
        viewModel.selectCurrentRecipientAndGetListWithoutAlreadySelected(args.currentPhoneNumber, args.alreadySelectedPhoneNumbers)
            .observe(viewLifecycleOwner) { list ->
                binding.tvListEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                listAdapter.recipients = list
            }
    }

    private fun selectContact() {
        askPermission(Manifest.permission.READ_CONTACTS){ result ->
            if (result) contactsLauncher.launch(null)
            else showToast(getString(R.string.permission_dined))
        }
    }

    private fun setSelected(selectedRecipient: String?) {
        setNavigationResult(selectedRecipient, REQ_SELECT_RECIPIENT)
        goBack()
    }
}




