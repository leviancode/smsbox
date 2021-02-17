package com.leviancode.android.gsmbox.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import androidx.databinding.DataBindingUtil
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.Recipient
import com.leviancode.android.gsmbox.data.model.RecipientGroup
import com.leviancode.android.gsmbox.data.model.RecipientGroupObservable
import com.leviancode.android.gsmbox.data.model.RecipientObservable
import com.leviancode.android.gsmbox.databinding.ListItemRecipientBinding
import com.leviancode.android.gsmbox.databinding.ListItemRecipientGroupBinding
import com.leviancode.android.gsmbox.ui.recipients.viewmodel.RecipientsViewModel


class RecipientGroupExpandableListAdapter(
    val context: Context,
    val viewModel: RecipientsViewModel,
    var parentList: List<RecipientGroup> = listOf(),
) : BaseExpandableListAdapter() {
    var data: List<Pair<RecipientGroup, List<Recipient>>> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getGroupCount() = data.size

    override fun getChildrenCount(groupPosition: Int) = data[groupPosition].second.size

    override fun getGroup(groupPosition: Int): Any = data[groupPosition].first

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return parentList[groupPosition].recipients[childPosition]
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
            binding?.group = RecipientGroupObservable(this.parentList[groupPosition])
            binding?.viewModel = viewModel
        } else {
            binding = DataBindingUtil.getBinding(convertView)
            binding?.group?.model = this.parentList[groupPosition]
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
        val model = this.parentList[groupPosition].recipients[childPosition]

        if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            binding = DataBindingUtil.inflate(inflater, R.layout.list_item_recipient, parent, false)
            binding?.run{
                recipient = RecipientObservable(model)
                viewModel = viewModel
                space.visibility = View.VISIBLE
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

