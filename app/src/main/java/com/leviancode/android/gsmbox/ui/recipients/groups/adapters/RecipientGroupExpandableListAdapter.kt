package com.leviancode.android.gsmbox.ui.recipients.groups.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import androidx.databinding.DataBindingUtil
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.recipients.GroupWithRecipients
import com.leviancode.android.gsmbox.data.model.recipients.Recipient
import com.leviancode.android.gsmbox.data.model.recipients.RecipientGroup
import com.leviancode.android.gsmbox.data.model.recipients.RecipientWithGroup
import com.leviancode.android.gsmbox.databinding.ListItemRecipientGroupBinding
import com.leviancode.android.gsmbox.databinding.ListItemRecipientInGroupBinding
import com.leviancode.android.gsmbox.ui.recipients.groups.list.RecipientGroupListViewModel


class RecipientGroupExpandableListAdapter(
    val context: Context,
    val viewModel: RecipientGroupListViewModel,
) : BaseExpandableListAdapter() {
    var data: List<GroupWithRecipients> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getGroupCount() = data.size

    override fun getChildrenCount(groupPosition: Int) = data[groupPosition].getRecipients().size

    override fun getGroup(groupPosition: Int): Any = data[groupPosition].group

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return data[groupPosition].getRecipients()[childPosition]
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
        val binding: ListItemRecipientGroupBinding
        if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            binding = DataBindingUtil.inflate(inflater, R.layout.list_item_recipient_group, parent, false)
            binding?.viewModel = viewModel
        } else {
            binding = DataBindingUtil.getBinding(convertView)!!
        }
        binding.model = data[groupPosition]
        binding.executePendingBindings()

       // binding.divider.visibility = if (isExpanded && data[groupPosition].recipients.isNotEmpty()) View.VISIBLE else View.INVISIBLE
        binding.cardRecipientGroup.elevation =
            if (isExpanded && data[groupPosition].getRecipients().isNotEmpty()) 8f
            else 0f

        return binding.root
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val binding: ListItemRecipientInGroupBinding? = if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            DataBindingUtil.inflate<ListItemRecipientInGroupBinding>(inflater, R.layout.list_item_recipient_in_group, parent, false)?.also {
                it.viewModel = viewModel
                it.space.visibility = View.VISIBLE
                it.cardRecipient.elevation = 8f
            }
        } else {
            DataBindingUtil.getBinding(convertView)
        }?.apply {
            val recipient = getChild(groupPosition, childPosition) as Recipient
            val group =  getGroup(groupPosition) as RecipientGroup
            model = RecipientWithGroup(recipient, group)
            executePendingBindings()
        }

        return binding!!.root
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}
