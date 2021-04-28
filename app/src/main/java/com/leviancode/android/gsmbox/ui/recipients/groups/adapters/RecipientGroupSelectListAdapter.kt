package com.leviancode.android.gsmbox.ui.recipients.groups.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.android.gsmbox.ui.recipients.groups.adapters.RecipientGroupSelectListAdapter.*
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.databinding.SelectListItemRecipientGroupBinding
import com.leviancode.android.gsmbox.ui.recipients.groups.dialog.RecipientGroupSelectListViewModel


class RecipientGroupSelectListAdapter(val viewModel: RecipientGroupSelectListViewModel) :
    RecyclerView.Adapter<RecipientGroupHolder>() {
    var groups: List<RecipientGroup> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipientGroupHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SelectListItemRecipientGroupBinding.inflate(
                inflater,
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
        private val binding: SelectListItemRecipientGroupBinding,
        val viewModel: RecipientGroupSelectListViewModel
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