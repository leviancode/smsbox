package com.leviancode.android.gsmbox.ui.fragments.recipients.recipients.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.android.gsmbox.core.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.databinding.MultiSelectListItemRecipientBinding
import com.leviancode.android.gsmbox.ui.fragments.recipients.recipients.dialog.RecipientMultiSelectListViewModel

class RecipientMultiSelectListAdapter (val viewModel: RecipientMultiSelectListViewModel) :
    RecyclerView.Adapter<RecipientMultiSelectListAdapter.RecipientHolder>() {
    var recipients: List<Recipient> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipientHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MultiSelectListItemRecipientBinding.inflate(
            inflater,
            parent,
            false
        )
        return RecipientHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: RecipientHolder, position: Int) {
        holder.bind(recipients[position])
    }

    override fun getItemCount() = recipients.size

    class RecipientHolder(
        private val binding: MultiSelectListItemRecipientBinding,
        val viewModel: RecipientMultiSelectListViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.viewModel = viewModel
        }

        fun bind(item: Recipient) {
            binding.model = item
            binding.executePendingBindings()
        }
    }
}