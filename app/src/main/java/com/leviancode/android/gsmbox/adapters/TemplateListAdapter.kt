package com.leviancode.android.gsmbox.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.ListItemTemplateGroupBinding
import com.leviancode.android.gsmbox.model.TemplateGroup
import com.leviancode.android.gsmbox.ui.templates.TemplateGroupViewModel

class TemplateListAdapter(private val items: ObservableList<TemplateGroup>) : RecyclerView.Adapter<TemplateListAdapter.TemplateGroupHolder>() {
    var callback: ObservableList.OnListChangedCallback<ObservableList<TemplateGroup>>? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        callback = ListChangeCallbacks()
        items.addOnListChangedCallback(callback)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        items.removeOnListChangedCallback(callback)
        callback = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateGroupHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ListItemTemplateGroupBinding =
            DataBindingUtil.inflate(inflater, R.layout.list_item_template_group, parent, false)
        return TemplateGroupHolder(binding)
    }

    override fun onBindViewHolder(holder: TemplateGroupHolder, position: Int) {
        val group = items[position]
        holder.bind(group)
    }

    override fun getItemCount(): Int {
        return items.size
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

    inner class ListChangeCallbacks : ObservableList.OnListChangedCallback<ObservableList<TemplateGroup>>() {
        override fun onChanged(sender: ObservableList<TemplateGroup>?) {
            notifyDataSetChanged()
        }

        override fun onItemRangeChanged(
            sender: ObservableList<TemplateGroup>?,
            positionStart: Int,
            itemCount: Int
        ) {
            notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeInserted(
            sender: ObservableList<TemplateGroup>?,
            positionStart: Int,
            itemCount: Int
        ) {
            notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeMoved(
            sender: ObservableList<TemplateGroup>?,
            fromPosition: Int,
            toPosition: Int,
            itemCount: Int
        ) {
            notifyDataSetChanged();
        }

        override fun onItemRangeRemoved(
            sender: ObservableList<TemplateGroup>?,
            positionStart: Int,
            itemCount: Int
        ) {
            notifyItemRangeRemoved(positionStart, itemCount);
        }

    }
}