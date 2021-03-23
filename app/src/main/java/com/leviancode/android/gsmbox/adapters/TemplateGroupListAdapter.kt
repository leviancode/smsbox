package com.leviancode.android.gsmbox.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.templates.TemplateGroup
import com.leviancode.android.gsmbox.databinding.ListItemTemplateGroupBinding
import com.leviancode.android.gsmbox.ui.templates.viewmodel.TemplateGroupListViewModel
import java.util.*

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

    fun moveItems(fromPosition: Int, toPosition: Int) {
        val newList = currentList.toMutableList()
        Collections.swap(newList, fromPosition, toPosition)
        submitList(newList)
    }

    class TemplateGroupHolder(
        private val binding: ListItemTemplateGroupBinding,
        val viewModel: TemplateGroupListViewModel
    ) : RecyclerView.ViewHolder(binding.root){

        init {
            binding.viewModel = viewModel
        }

        fun bind(group: TemplateGroup) {
            binding.model = group
            binding.executePendingBindings()
        }
    }

    class GroupListDiffCallback : DiffUtil.ItemCallback<TemplateGroup>() {
        override fun areItemsTheSame(oldItem: TemplateGroup, newItem: TemplateGroup): Boolean {
            return oldItem.templateGroupId == newItem.templateGroupId
        }

        override fun areContentsTheSame(oldItem: TemplateGroup, newItem: TemplateGroup): Boolean {
            return oldItem == newItem
        }
    }
}