package com.leviancode.android.gsmbox.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.Template
import com.leviancode.android.gsmbox.data.model.TemplateGroup
import com.leviancode.android.gsmbox.databinding.ListItemTemplateGroupBinding
import com.leviancode.android.gsmbox.data.model.TemplateGroupObservable
import com.leviancode.android.gsmbox.ui.templates.viewmodel.TemplateGroupListViewModel

class TemplateGroupListAdapter(val viewModel: TemplateGroupListViewModel) :
    ListAdapter<TemplateGroup, TemplateGroupListAdapter.TemplateGroupHolder>(
        GroupListDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateGroupHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ListItemTemplateGroupBinding =
            DataBindingUtil.inflate(inflater, R.layout.list_item_template_group, parent, false)
        return TemplateGroupHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: TemplateGroupHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TemplateGroupHolder(
        private val binding: ListItemTemplateGroupBinding,
        val viewModel: TemplateGroupListViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.group = TemplateGroupObservable()
        }

        fun bind(group: TemplateGroup) {
            binding.viewModel = viewModel
            binding.group?.data = group
            binding.executePendingBindings()
        }
    }

    class GroupListDiffCallback : DiffUtil.ItemCallback<TemplateGroup>() {
        override fun areItemsTheSame(oldItem: TemplateGroup, newItem: TemplateGroup): Boolean {
            return oldItem.groupId == newItem.groupId
        }

        override fun areContentsTheSame(oldItem: TemplateGroup, newItem: TemplateGroup): Boolean {
            return oldItem == newItem
        }
    }
}