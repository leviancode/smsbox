package com.leviancode.android.gsmbox.ui.screens.recipients.recipients.dialog

import android.Manifest
import android.view.View
import android.widget.FrameLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentManager
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.DialogSelectListBinding
import com.leviancode.android.gsmbox.databinding.SelectListItemRecipientBinding
import com.leviancode.android.gsmbox.databinding.ViewButtonContactsBinding
import com.leviancode.android.gsmbox.ui.base.BaseBottomSheet
import com.leviancode.android.gsmbox.ui.base.BaseListAdapter
import com.leviancode.android.gsmbox.ui.entities.recipients.RecipientUI
import com.leviancode.android.gsmbox.utils.extensions.askPermission
import com.leviancode.android.gsmbox.utils.extensions.observe
import com.leviancode.android.gsmbox.utils.extensions.showToast
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




