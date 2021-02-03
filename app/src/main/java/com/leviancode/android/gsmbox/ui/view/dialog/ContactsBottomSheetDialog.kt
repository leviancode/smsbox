package com.leviancode.android.gsmbox.ui.view.dialog

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.github.tamir7.contacts.Contact
import com.github.tamir7.contacts.Contacts
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.ContactListAdapter
import com.leviancode.android.gsmbox.adapters.ListItemClickListener
import com.leviancode.android.gsmbox.databinding.DialogBottomSheetContactsBinding
import com.leviancode.android.gsmbox.utils.setNavigationResult


class ContactsBottomSheetDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogBottomSheetContactsBinding
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            fetchContacts()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.permission_dined),
                Toast.LENGTH_SHORT
            ).show()
            closeDialog()
        }
    }

    override fun getTheme() = R.style.CustomBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.dialog_bottom_sheet_contacts, container, false
        )
        checkPermission()
        binding.ibCloseDialog.setOnClickListener { closeDialog() }
        return binding.root
    }

    private fun selectContact(contact: Contact) {
        setNavigationResult(contact.phoneNumbers[0].number, NewRecipientDialog.SAVED_REQUEST_KEY)
        closeDialog()
    }

    private fun fetchContacts() {
        Contacts.initialize(requireContext())
        val q = Contacts.getQuery()
        q.hasPhoneNumber()
        val contacts: List<Contact> = q.find()
        val listAdapter = ContactListAdapter(contacts, ListItemClickListener { selectContact(it) })
        binding.bottomSheetContactList.adapter = listAdapter
    }

    fun checkPermission() {
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_CONTACTS
            )
        ) {
            fetchContacts()
        } else {
            requestPermissionLauncher.launch(
                Manifest.permission.READ_CONTACTS
            )
        }
    }

    private fun closeDialog() {
        requireParentFragment().findNavController().navigateUp()
    }

    companion object{
        val TAG = ContactsBottomSheetDialog::class.java.simpleName
        const val SAVED_REQUEST_KEY = "isSaved"
    }
}