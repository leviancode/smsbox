package com.leviancode.android.gsmbox.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.Template
import com.leviancode.android.gsmbox.data.model.TemplateObservable
import com.leviancode.android.gsmbox.databinding.ListItemTemplateBinding
import com.leviancode.android.gsmbox.ui.viewmodel.TemplateListViewModel
import com.leviancode.android.gsmbox.ui.viewmodel.TemplateViewModel

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

    class TemplateHolder(
        val binding: ListItemTemplateBinding,
        val viewModel: TemplateListViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.template = TemplateObservable()
            binding.viewModel = viewModel
        }

        fun bind(item: Template) {
            binding.template?.data = item
            binding.executePendingBindings()
        }
    }

    class TemplateDiffCallback : DiffUtil.ItemCallback<Template>() {
        override fun areItemsTheSame(oldItem: Template, newItem: Template): Boolean {
            return oldItem.templateId == newItem.templateId
        }

        override fun areContentsTheSame(oldItem: Template, newItem: Template): Boolean {
            return oldItem.name == newItem.name
                    && oldItem.message == newItem.message
                    && oldItem.iconColor == newItem.iconColor
        }
    }
}
