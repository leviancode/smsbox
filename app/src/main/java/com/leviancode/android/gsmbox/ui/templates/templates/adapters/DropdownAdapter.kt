package com.leviancode.android.gsmbox.ui.templates.templates.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import com.leviancode.android.gsmbox.data.model.placeholders.Placeholder
import com.leviancode.android.gsmbox.databinding.ViewAutocompletePlaceholderDropdownBinding
import java.util.*
import kotlin.collections.ArrayList

class DropdownAdapter(val context: Context, val items: List<Placeholder>) : BaseAdapter(), Filterable {
    private val suggestions: ArrayList<Placeholder> = ArrayList()
    private val filter: Filter = CustomFilter()

    override fun getCount(): Int  = suggestions.size

    override fun getItem(position: Int) = suggestions[position].getName()

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var binding: ViewAutocompletePlaceholderDropdownBinding? = null
        val holder = if(convertView == null) {
            binding = ViewAutocompletePlaceholderDropdownBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            DropdownItemHolder(binding).also {
                binding.root.tag = it
            }
        } else {
            convertView.tag as DropdownItemHolder
        }
        holder.binding.placeholderName.text = suggestions[position].getName()
        holder.binding.placeholderValue.text = suggestions[position].getValue()

        if (binding == null) return convertView
        return holder.binding.root
    }

    override fun getFilter(): Filter = filter

    class DropdownItemHolder(val binding: ViewAutocompletePlaceholderDropdownBinding)

    inner class CustomFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            suggestions.clear()
            for (i in items.indices) {
                if (items[i].getName().toLowerCase(Locale.ROOT).contains(constraint)
                ) {
                    suggestions.add(items[i])
                }
            }
            val results = FilterResults()
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

