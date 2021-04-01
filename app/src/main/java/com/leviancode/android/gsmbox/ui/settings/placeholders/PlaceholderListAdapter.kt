package com.leviancode.android.gsmbox.ui.settings.placeholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.placeholders.Placeholder
import com.leviancode.android.gsmbox.data.model.templates.TemplateGroup
import com.leviancode.android.gsmbox.databinding.ListItemPlaceholderBinding
import com.leviancode.android.gsmbox.ui.settings.placeholders.PlaceholderListAdapter.PlaceholderViewHolder

class PlaceholderListAdapter(val viewModel: PlaceholdersViewModel) :
    ListAdapter<Placeholder, PlaceholderViewHolder>(PlaceholdersDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceholderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ListItemPlaceholderBinding =
            DataBindingUtil.inflate(inflater, R.layout.list_item_placeholder, parent, false)
        return PlaceholderViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: PlaceholderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PlaceholderViewHolder(
        val binding: ListItemPlaceholderBinding,
        val viewModel: PlaceholdersViewModel,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.viewModel = viewModel
        }

        fun bind(item: Placeholder) {
            binding.model = item
            binding.executePendingBindings()
        }
    }

    class PlaceholdersDiffCallback : DiffUtil.ItemCallback<Placeholder>() {
        override fun areItemsTheSame(oldItem: Placeholder, newItem: Placeholder): Boolean {
            return oldItem.placeholderId == newItem.placeholderId
        }

        override fun areContentsTheSame(oldItem: Placeholder, newItem: Placeholder): Boolean {
            return oldItem == newItem
        }
    }
}