package com.leviancode.android.gsmbox.ui.screens.recipients.recipients.dialog

import android.Manifest
import android.view.View
import android.widget.FrameLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.navArgs
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.entities.recipients.RecipientData
import com.leviancode.android.gsmbox.utils.REQ_SELECT_RECIPIENT
import com.leviancode.android.gsmbox.databinding.DialogSelectListBinding
import com.leviancode.android.gsmbox.databinding.SelectListItemRecipientBinding
import com.leviancode.android.gsmbox.databinding.ViewButtonContactsBinding
import com.leviancode.android.gsmbox.ui.base.GenericListAdapter
import com.leviancode.android.gsmbox.ui.base.BaseBottomSheet
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientUI
import com.leviancode.android.gsmbox.utils.extensions.askPermission
import com.leviancode.android.gsmbox.utils.extensions.navigateBack
import com.leviancode.android.gsmbox.utils.extensions.setNavigationResult
import com.leviancode.android.gsmbox.utils.extensions.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipientSelectListDialog : BaseBottomSheet<DialogSelectListBinding>(R.layout.dialog_select_list) {
    private val viewModel: RecipientSelectListViewModel by viewModel()
    private val args: RecipientSelectListDialogArgs by navArgs()
    private val listAdapter =
        GenericListAdapter<RecipientUI, SelectListItemRecipientBinding>(R.layout.select_list_item_recipient) { item, binding ->
            binding.viewModel = viewModel
            binding.model = item
        }
    private lateinit var contactsLauncher: ActivityResultLauncher<Void>

    override fun onCreated() {
        contactsLauncher =
            registerForActivityResult(ActivityResultContracts.PickContact()) { uri ->
                viewModel.selectRecipientByContactUri(uri)
                setSelected(viewModel.selectedRecipient)
            }
        updateTitle()
        observeEvents()
    }

    private fun updateTitle() {
        binding.toolbar.title = getString(R.string.select_recipient)
    }

    private fun observeEvents() {
        requireDialog().setOnShowListener {
            binding.recyclerView.adapter = listAdapter

            val container = requireDialog().findViewById<FrameLayout>(R.id.container)
            val bind = ViewButtonContactsBinding.inflate(requireDialog().layoutInflater)
            container?.addView(bind.root)
            bind.btnContacts.setOnClickListener {
                selectContact()
            }
            observeData()
        }
        requireDialog().setOnDismissListener {
            setSelected(null)
        }
        binding.btnOk.setOnClickListener {
            setSelected(viewModel.selectedRecipient)
        }

    }

    private fun observeData() {
        viewModel.selectCurrentRecipientAndGetListWithoutAlreadySelected(args.currentPhoneNumber, args.alreadySelectedPhoneNumbers)
            .observe(viewLifecycleOwner) { list ->
                binding.tvListEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                listAdapter.submitList(list)
            }
    }

    private fun selectContact() {
        askPermission(Manifest.permission.READ_CONTACTS){ result ->
            if (result) contactsLauncher.launch(null)
            else showToast(getString(R.string.permission_dined))
        }
    }

    private fun setSelected(selectedRecipient: RecipientUI?) {
        setNavigationResult(selectedRecipient, REQ_SELECT_RECIPIENT)
        navigateBack()
    }
}




