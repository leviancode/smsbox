package com.leviancode.android.gsmbox.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.RecipientListAdapter.*
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.databinding.ListItemRecipientBinding
import com.leviancode.android.gsmbox.ui.recipients.viewmodel.RecipientsViewModel
import java.util.*

class RecipientListAdapter(val viewModel: RecipientsViewModel) :
    ListAdapter<Recipient, RecipientHolder>(RecipientsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipientHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ListItemRecipientBinding =
            DataBindingUtil.inflate(inflater, R.layout.list_item_recipient, parent, false)
        return RecipientHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: RecipientHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun moveItems(fromPosition: Int, toPosition: Int) {
        val newList = currentList.toMutableList()
        Collections.swap(newList, fromPosition, toPosition)
        submitList(newList)
    }

    class RecipientHolder(
        val binding: ListItemRecipientBinding,
        val viewModel: RecipientsViewModel
    ) : RecyclerView.ViewHolder(binding.root) {


        init {
            binding.viewModel = viewModel
            binding.space.visibility = View.GONE
        }

        fun bind(item: Recipient) {
            binding.recipient = item
            binding.executePendingBindings()
        }
    }

    class RecipientsDiffCallback : DiffUtil.ItemCallback<Recipient>() {
        override fun areItemsTheSame(oldItem: Recipient, newItem: Recipient): Boolean {
            return oldItem.recipientId == newItem.recipientId
        }

        override fun areContentsTheSame(oldItem: Recipient, newItem: Recipient): Boolean {
            return oldItem == newItem
        }
    }
}