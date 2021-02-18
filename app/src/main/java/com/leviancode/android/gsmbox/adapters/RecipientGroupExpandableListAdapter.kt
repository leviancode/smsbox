package com.leviancode.android.gsmbox.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import androidx.databinding.DataBindingUtil
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.*
import com.leviancode.android.gsmbox.databinding.ListItemRecipientBinding
import com.leviancode.android.gsmbox.databinding.ListItemRecipientGroupBinding
import com.leviancode.android.gsmbox.ui.recipients.viewmodel.RecipientsViewModel


class RecipientGroupExpandableListAdapter(
    val context: Context,
    val viewModel: RecipientsViewModel,
) : BaseExpandableListAdapter() {
    var data: List<RecipientGroupWithRecipients> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getGroupCount() = data.size

    override fun getChildrenCount(groupPosition: Int) = data[groupPosition].recipients.size

    override fun getGroup(groupPosition: Int): Any = data[groupPosition].group

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return data[groupPosition].recipients[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds() = true

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var binding: ListItemRecipientGroupBinding?

        if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            binding = DataBindingUtil.inflate(inflater, R.layout.list_item_recipient_group, parent, false)
            binding?.group = RecipientGroupObservable(data[groupPosition].group)
            binding?.viewModel = viewModel
        } else {
            binding = DataBindingUtil.getBinding(convertView)
            binding?.group?.model = data[groupPosition].group
        }
        binding?.executePendingBindings()

        return binding?.root!!
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var binding: ListItemRecipientBinding?
        val model = data[groupPosition].recipients[childPosition]

        if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            binding = DataBindingUtil.inflate(inflater, R.layout.list_item_recipient, parent, false)
            binding?.let{
                it.recipient = RecipientObservable(model)
                it.viewModel = viewModel
                it.space.visibility = View.VISIBLE
            }
        } else {
            binding = DataBindingUtil.getBinding(convertView)
            binding?.recipient?.model = model
        }

        binding?.divider?.visibility = if (isLastChild) View.VISIBLE else View.GONE

        binding?.executePendingBindings()

        return binding?.root!!
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}

