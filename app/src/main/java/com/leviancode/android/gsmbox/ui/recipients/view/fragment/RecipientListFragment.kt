package com.leviancode.android.gsmbox.ui.recipients.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.RecipientListAdapter
import com.leviancode.android.gsmbox.databinding.FragmentRecipientListBinding
import com.leviancode.android.gsmbox.helpers.ItemDragHelperCallback
import com.leviancode.android.gsmbox.helpers.ItemDragListener
import com.leviancode.android.gsmbox.ui.recipients.viewmodel.RecipientsViewModel

class RecipientListFragment : Fragment(), ItemDragListener {
    private lateinit var binding: FragmentRecipientListBinding
    private val viewModel: RecipientsViewModel by activityViewModels()
    private lateinit var listAdapter: RecipientListAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipient_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listAdapter = RecipientListAdapter(viewModel)
        binding.adapter = listAdapter
        itemTouchHelper = ItemTouchHelper(ItemDragHelperCallback(this)).apply {
            attachToRecyclerView(binding.recipientsRecyclerView)
        }

        observeUI()
    }

    private fun observeUI() {
        viewModel.recipients.observe(viewLifecycleOwner){
            listAdapter.submitList(it)
            listAdapter.notifyDataSetChanged()
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        listAdapter.moveItems(fromPosition, toPosition)
    }

    override fun onMoveFinished() {
        viewModel.updateAllRecipients(listAdapter.currentList)
    }
}