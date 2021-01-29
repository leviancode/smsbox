package com.leviancode.android.gsmbox.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.TemplateGroup
import com.leviancode.android.gsmbox.databinding.ListItemTemplateGroupBinding
import com.leviancode.android.gsmbox.ui.viewmodel.TemplateGroupObservable

class TemplateGroupListAdapter : ListAdapter<TemplateGroup, TemplateGroupListAdapter.TemplateGroupHolder>(
    TemplatesDiffCallback<TemplateGroup>()
) {
    var clickListener: ListItemClickListener<TemplateGroup>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateGroupHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ListItemTemplateGroupBinding =
            DataBindingUtil.inflate(inflater, R.layout.list_item_template_group, parent, false)
        clickListener?.let { binding.clickListener = it }
        return TemplateGroupHolder(binding)
    }

    override fun onBindViewHolder(holder: TemplateGroupHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TemplateGroupHolder(private val binding: ListItemTemplateGroupBinding)
        : RecyclerView.ViewHolder(binding.root){

        init {
            binding.group = TemplateGroupObservable()
        }

        fun bind(group: TemplateGroup){
            binding.group?.group = group
            binding.executePendingBindings()
        }
    }
}