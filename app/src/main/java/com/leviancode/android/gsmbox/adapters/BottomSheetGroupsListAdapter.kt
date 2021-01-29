package com.leviancode.android.gsmbox.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.TemplateGroup
import com.leviancode.android.gsmbox.databinding.BottomSheetGroupsItemBinding

class BottomSheetGroupsListAdapter(val items: List<TemplateGroup>) : RecyclerView.Adapter<BottomSheetGroupsListAdapter.ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: BottomSheetGroupsItemBinding = DataBindingUtil.inflate(
            inflater, R.layout.bottom_sheet_groups_item, parent, false)
        return ItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ItemHolder(private val binding: BottomSheetGroupsItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(group: TemplateGroup){
            binding.itemLabel.text = group.name
            if (group.imageUri != null){
                binding.itemIcon.load(group.imageUri)
            } else {
                binding.itemIcon.load(R.drawable.ic_baseline_library_books_24)
                binding.itemIcon.setColorFilter(group.iconColor)
            }
        }
    }
}