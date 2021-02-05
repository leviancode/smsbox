package com.leviancode.android.gsmbox.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.Template
import com.leviancode.android.gsmbox.databinding.ListItemTemplateBinding
import com.leviancode.android.gsmbox.ui.viewmodel.TemplateViewModel

class TemplateListAdapter : ListAdapter<Template, TemplateListAdapter.TemplateHolder>(
    ListItemDiffCallback<Template>()
) {
    var clickListener: ListItemClickListener<Template>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ListItemTemplateBinding =
            DataBindingUtil.inflate(inflater, R.layout.list_item_template, parent, false)
        clickListener?.let { binding.clickListener = it }
        return TemplateHolder(binding)
    }

    override fun onBindViewHolder(holder: TemplateHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TemplateHolder(val binding: ListItemTemplateBinding) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel = TemplateViewModel()
        init {
            binding.viewModel = viewModel
        }

        fun bind(template: Template){
            viewModel.setData(template)
            binding.executePendingBindings()
        }
    }
}
