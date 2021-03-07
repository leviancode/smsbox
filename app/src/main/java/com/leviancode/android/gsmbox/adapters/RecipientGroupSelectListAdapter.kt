package com.leviancode.android.gsmbox.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.RecipientGroupSelectListAdapter.*
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroupObservable
import com.leviancode.android.gsmbox.databinding.SelectListItemRecipientGroupBinding
import com.leviancode.android.gsmbox.ui.recipients.viewmodel.RecipientGroupSelectListViewModel

class RecipientGroupSelectListAdapter(val viewModel: RecipientGroupSelectListViewModel) :
    RecyclerView.Adapter<RecipientGroupHolder>() {
    var groups: List<RecipientGroupObservable> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipientGroupHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: SelectListItemRecipientGroupBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.select_list_item_recipient_group,
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

        fun bind(item: RecipientGroupObservable) {
            binding.group = item
            binding.executePendingBindings()
        }
    }


}