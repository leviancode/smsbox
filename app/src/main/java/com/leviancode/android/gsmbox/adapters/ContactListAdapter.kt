package com.leviancode.android.gsmbox.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.tamir7.contacts.Contact
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.ListItemContactBinding

class ContactListAdapter(
    val contacts: List<Contact>,
    val listener: ListItemClickListener<Contact>
) : RecyclerView.Adapter<ContactListAdapter.ContactHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ListItemContactBinding =
            DataBindingUtil.inflate(inflater, R.layout.list_item_contact, parent, false)
        binding.clickListener = listener
        return ContactHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount() = contacts.size

    class ContactHolder(val binding: ListItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            binding.contact = contact
            binding.editTextContactName.text = contact.displayName
            binding.editTextContactNumber.text = contact.phoneNumbers[0].number.toString()
        }
    }


}