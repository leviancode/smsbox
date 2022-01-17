package com.brainymobile.android.smsbox.ui.screens.recipients.recipients.dialog

import android.Manifest
import android.view.View
import android.widget.FrameLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentManager
import com.brainymobile.android.smsbox.R
import com.brainymobile.android.smsbox.databinding.DialogSelectListBinding
import com.brainymobile.android.smsbox.databinding.SelectListItemRecipientBinding
import com.brainymobile.android.smsbox.databinding.ViewButtonContactsBinding
import com.brainymobile.android.smsbox.ui.base.BaseBottomSheet
import com.brainymobile.android.smsbox.ui.base.BaseListAdapter
import com.brainymobile.android.smsbox.ui.entities.recipients.RecipientUI
import com.brainymobile.android.smsbox.utils.extensions.askPermission
import com.brainymobile.android.smsbox.utils.extensions.observe
import com.brainymobile.android.smsbox.utils.extensions.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipientSelectListDialog private constructor(
    private val currentPhoneNumber: String,
    private val selectedPhoneNumbers: List<String>,
    private val callback: (RecipientUI) -> Unit
) : BaseBottomSheet<DialogSelectListBinding>(R.layout.dialog_select_list) {
    private val viewModel: RecipientSelectListViewModel by viewModel()
    private val listAdapter =
        BaseListAdapter<RecipientUI, SelectListItemRecipientBinding>(R.layout.select_list_item_recipient) { binding, item, position ->
            binding.viewModel = viewModel
            binding.model = item
        }
    private lateinit var contactsLauncher: ActivityResultLauncher<Void>

    override fun onCreated() {
        contactsLauncher =
            registerForActivityResult(ActivityResultContracts.PickContact()) { uri ->
                uri?.let {
                    viewModel.selectRecipientByContactUri(uri)
                    setSelected(viewModel.selectedRecipient)
                }
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
        viewModel.selectCurrentRecipientAndGetListWithoutAlreadySelected(
            currentPhoneNumber,
            selectedPhoneNumbers
        )
            .observe(viewLifecycleOwner) { list ->
                binding.tvListEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                listAdapter.submitList(list)
            }
    }

    private fun selectContact() {
        askPermission(Manifest.permission.READ_CONTACTS) { result ->
            if (result) contactsLauncher.launch(null)
            else showToast(getString(R.string.permission_dined))
        }
    }

    private fun setSelected(selectedRecipient: RecipientUI?) {
        selectedRecipient?.let(callback)
        close()
    }

    companion object {
        fun show(
            fm: FragmentManager,
            currentPhoneNumber: String,
            selectedPhoneNumbers: List<String>,
            callback: (RecipientUI) -> Unit
        ) {
            RecipientSelectListDialog(currentPhoneNumber, selectedPhoneNumbers, callback).show(
                fm,
                null
            )
        }
    }
}




