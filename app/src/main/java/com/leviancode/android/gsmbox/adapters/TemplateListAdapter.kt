package com.leviancode.android.gsmbox.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.templates.Template
import com.leviancode.android.gsmbox.databinding.ListItemTemplateBinding
import com.leviancode.android.gsmbox.ui.templates.viewmodel.TemplateListViewModel
import java.util.*

class TemplateListAdapter(val viewModel: TemplateListViewModel) :
    ListAdapter<Template, TemplateListAdapter.TemplateHolder>(TemplateDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ListItemTemplateBinding =
            DataBindingUtil.inflate(inflater, R.layout.list_item_template, parent, false)
        return TemplateHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: TemplateHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun moveItems(fromPosition: Int, toPosition: Int) {
        val newList = currentList.toMutableList()
        Collections.swap(newList, fromPosition, toPosition)
        submitList(newList)
    }

    class TemplateHolder(
        val binding: ListItemTemplateBinding,
        val viewModel: TemplateListViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.viewModel = viewModel
        }

        fun bind(item: Template) {
            binding.model = item
            binding.executePendingBindings()
        }
    }

    class TemplateDiffCallback : DiffUtil.ItemCallback<Template>() {
        override fun areItemsTheSame(oldItem: Template, newItem: Template): Boolean {
            return oldItem.templateId == newItem.templateId
        }

        override fun areContentsTheSame(oldItem: Template, newItem: Template): Boolean {
            return oldItem.getName() == newItem.getName()
                    && oldItem.getMessage() == newItem.getMessage()
                    && oldItem.getIconColor() == newItem.getIconColor()
                    && oldItem.getRecipientsAsString() == newItem.getRecipientsAsString()
        }
    }
}
