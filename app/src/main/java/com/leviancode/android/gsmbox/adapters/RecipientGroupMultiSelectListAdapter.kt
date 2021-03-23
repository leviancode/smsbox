package com.leviancode.android.gsmbox.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.RecipientGroupMultiSelectListAdapter.*
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.databinding.MultiSelectListItemRecipientGroupBinding
import com.leviancode.android.gsmbox.ui.recipients.viewmodel.RecipientGroupMultiSelectListViewModel
import com.leviancode.android.gsmbox.ui.recipients.viewmodel.RecipientGroupSelectListViewModel

class RecipientGroupMultiSelectListAdapter(val viewModel: RecipientGroupMultiSelectListViewModel) :
    RecyclerView.Adapter<RecipientGroupHolder>() {
    var groups: List<RecipientGroup> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipientGroupHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: MultiSelectListItemRecipientGroupBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.multi_select_list_item_recipient_group,
                parent,
                false
            )
        return RecipientGroupHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: RecipientGroupHolder, position: Int) {
        holder.bind(groups[position])
    }

    override fun getItemCount() = groups.size

    class RecipientGroupHolder(
        private val binding: MultiSelectListItemRecipientGroupBinding,
        val viewModel: RecipientGroupMultiSelectListViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.viewModel = viewModel
        }

        fun bind(item: RecipientGroup) {
            binding.model = item
            binding.executePendingBindings()
        }
    }
}