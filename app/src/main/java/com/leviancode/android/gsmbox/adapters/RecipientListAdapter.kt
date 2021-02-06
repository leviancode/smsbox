package com.leviancode.android.gsmbox.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.Recipient
import com.leviancode.android.gsmbox.databinding.ListItemRecipientBinding
import com.leviancode.android.gsmbox.ui.recipients.viewmodel.RecipientViewModel

class RecipientListAdapter : ListAdapter<Recipient, RecipientListAdapter.RecipientHolder>(ListItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipientHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ListItemRecipientBinding =
            DataBindingUtil.inflate(inflater, R.layout.list_item_recipient, parent, false)
        return RecipientHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipientHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RecipientHolder(val binding: ListItemRecipientBinding) : RecyclerView.ViewHolder(binding.root){
        private val viewModel = RecipientViewModel()
        init {
            binding.viewModel = viewModel
        }

        fun bind(recipient: Recipient) {
            viewModel.setData(recipient)
            binding.executePendingBindings()
        }
    }
}