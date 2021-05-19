package com.leviancode.android.gsmbox.ui.fragments.templates.groups.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.core.data.model.templates.GroupWithTemplates
import com.leviancode.android.gsmbox.databinding.ListItemTemplateGroupBinding
import com.leviancode.android.gsmbox.ui.fragments.templates.groups.list.TemplateGroupListViewModel
import java.util.*

class TemplateGroupListAdapter(val viewModel: TemplateGroupListViewModel) :
    ListAdapter<GroupWithTemplates, TemplateGroupListAdapter.TemplateGroupHolder>(
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

    fun moveItems(fromPosition: Int, toPosition: Int): Pair<Int, Int> {
        val newList = currentList.toMutableList()
        Collections.swap(newList, fromPosition, toPosition)
        submitList(newList)
        return getItem(fromPosition).group.templateGroupId to getItem(toPosition).group.templateGroupId
    }

    class TemplateGroupHolder(
        private val binding: ListItemTemplateGroupBinding,
        val viewModel: TemplateGroupListViewModel
    ) : RecyclerView.ViewHolder(binding.root){

        init {
            binding.viewModel = viewModel
        }

        fun bind(group: GroupWithTemplates) {
            binding.model = group
            binding.executePendingBindings()
        }
    }

    class GroupListDiffCallback : DiffUtil.ItemCallback<GroupWithTemplates>() {
        override fun areItemsTheSame(oldItem: GroupWithTemplates, newItem: GroupWithTemplates): Boolean {
            return oldItem.group.templateGroupId == newItem.group.templateGroupId
        }

        override fun areContentsTheSame(oldItem: GroupWithTemplates, newItem: GroupWithTemplates): Boolean {
            return oldItem == newItem
        }
    }
}