package com.leviancode.android.gsmbox.ui.templates.templates.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.templates.TemplateWithRecipients
import com.leviancode.android.gsmbox.databinding.ListItemTemplateBinding
import com.leviancode.android.gsmbox.ui.templates.templates.list.TemplateListViewModel
import java.util.*

class TemplateListAdapter(val viewModel: TemplateListViewModel) :
    ListAdapter<TemplateWithRecipients, TemplateListAdapter.TemplateHolder>(TemplateDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ListItemTemplateBinding =
            DataBindingUtil.inflate(inflater, R.layout.list_item_template, parent, false)
        return TemplateHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: TemplateHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun moveItems(fromPosition: Int, toPosition: Int): Pair<Int, Int> {
        val newList = currentList.toMutableList()
        Collections.swap(newList, fromPosition, toPosition)
        submitList(newList)
        return getItem(fromPosition).template.templateId to getItem(toPosition).template.templateId
    }

    class TemplateHolder(
        val binding: ListItemTemplateBinding,
        val viewModel: TemplateListViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.viewModel = viewModel
        }

        fun bind(item: TemplateWithRecipients) {
            binding.model = item
            binding.executePendingBindings()
        }
    }

    class TemplateDiffCallback : DiffUtil.ItemCallback<TemplateWithRecipients>() {
        override fun areItemsTheSame(oldItem: TemplateWithRecipients, newItem: TemplateWithRecipients): Boolean {
            return oldItem.template.templateId == newItem.template.templateId
        }

        override fun areContentsTheSame(oldItem: TemplateWithRecipients, newItem: TemplateWithRecipients): Boolean {
            return oldItem == newItem
        }
    }
}
