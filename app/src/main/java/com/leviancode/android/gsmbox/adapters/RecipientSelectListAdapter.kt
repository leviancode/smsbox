package com.leviancode.android.gsmbox.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.RecipientSelectListAdapter.*
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.databinding.SelectListItemRecipientBinding
import com.leviancode.android.gsmbox.ui.recipients.viewmodel.RecipientSelectListViewModel

class RecipientSelectListAdapter(val viewModel: RecipientSelectListViewModel) :
    RecyclerView.Adapter<RecipientHolder>() {
    var recipients: List<Recipient> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipientHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: SelectListItemRecipientBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.select_list_item_recipient,
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
        private val binding: SelectListItemRecipientBinding,
        val viewModel: RecipientSelectListViewModel
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