package com.leviancode.android.gsmbox.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.ListItemTemplateGroupBinding
import com.leviancode.android.gsmbox.model.TemplateGroup
import com.leviancode.android.gsmbox.ui.templates.TemplateGroupViewModel

class TemplateListAdapter : ListAdapter<TemplateGroup, TemplateListAdapter.TemplateGroupHolder>(TemplateGroupDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateGroupHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ListItemTemplateGroupBinding =
            DataBindingUtil.inflate(inflater, R.layout.list_item_template_group, parent, false)
        return TemplateGroupHolder(binding)
    }

    override fun onBindViewHolder(holder: TemplateGroupHolder, position: Int) {
        val group = getItem(position)
        holder.bind(group)
    }

    class TemplateGroupHolder(private val binding: ListItemTemplateGroupBinding)
        : RecyclerView.ViewHolder(binding.root){

        init {
            binding.viewModel = TemplateGroupViewModel()
        }

        fun bind(group: TemplateGroup){
            binding.viewModel?.group = group
            binding.executePendingBindings()
        }
    }

    class TemplateGroupDiffCallback : DiffUtil.ItemCallback<TemplateGroup>(){
        override fun areItemsTheSame(oldItem: TemplateGroup, newItem: TemplateGroup): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TemplateGroup, newItem: TemplateGroup): Boolean {
            return oldItem == newItem
        }

    }
}