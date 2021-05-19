package com.leviancode.android.gsmbox.ui.fragments.contacts

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.tamir7.contacts.Contact
import com.github.tamir7.contacts.Contacts
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.DialogSelectListContactsBinding
import com.leviancode.android.gsmbox.core.utils.REQ_SELECTED
import com.leviancode.android.gsmbox.core.utils.extensions.goBack
import com.leviancode.android.gsmbox.core.utils.extensions.setNavigationResult


class ContactListDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogSelectListContactsBinding

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
            inflater, R.layout.dialog_select_list_contacts, container, false
        )
        binding.ibCloseDialog.setOnClickListener { goBack() }
        fetchContacts()
        return binding.root
    }

    private fun selectContact(contact: Contact) {
        setNavigationResult(contact.phoneNumbers[0].number, REQ_SELECTED)
        goBack()
    }

    private fun fetchContacts() {
        Contacts.initialize(requireContext())
        val q = Contacts.getQuery()
        q.hasPhoneNumber()
        val contacts: List<Contact> = q.find()
        val listAdapter = ContactListAdapter(contacts) { selectContact(it) }
        binding.bottomSheetContactList.adapter = listAdapter
    }
}