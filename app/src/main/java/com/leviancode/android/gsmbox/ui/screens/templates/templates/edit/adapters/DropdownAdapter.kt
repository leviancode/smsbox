package com.leviancode.android.gsmbox.ui.screens.templates.templates.edit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import com.leviancode.android.gsmbox.databinding.ViewAutocompletePlaceholderDropdownBinding
import com.leviancode.android.gsmbox.ui.entities.placeholder.PlaceholderUI
import java.util.*

class DropdownAdapter(val context: Context, val items: List<PlaceholderUI>) : BaseAdapter(),
    Filterable {
    private val suggestions: ArrayList<PlaceholderUI> = ArrayList()
    private val filter: Filter = CustomFilter()

    override fun getCount(): Int = suggestions.size

    override fun getItem(position: Int) = suggestions[position].nameWithHashTag

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var binding: ViewAutocompletePlaceholderDropdownBinding? = null
        val holder = if (convertView == null) {
            binding = ViewAutocompletePlaceholderDropdownBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            DropdownItemHolder(binding).also {
                binding.root.tag = it
            }
        } else {
            convertView.tag as DropdownItemHolder
        }
        holder.binding.model = suggestions[position]
        holder.binding.executePendingBindings()

        if (binding == null) return convertView
        return holder.binding.root
    }

    override fun getFilter(): Filter = filter

    class DropdownItemHolder(val binding: ViewAutocompletePlaceholderDropdownBinding)

    inner class CustomFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            suggestions.clear()
            if (constraint != null){
                for (i in items.indices) {
                    if (constraint.contains("#")
                        && items[i].nameWithHashTag.lowercase(Locale.ROOT).contains(constraint)
                    ) {
                        suggestions.add(items[i])
                    }
                }
            }

            results.values = suggestions
            results.count = suggestions.size
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            if (results.count > 0) {
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }
    }
}

