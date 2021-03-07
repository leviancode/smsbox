package com.leviancode.android.gsmbox.ui.templates.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.TemplateListAdapter
import com.leviancode.android.gsmbox.data.model.templates.Template
import com.leviancode.android.gsmbox.databinding.FragmentTemplateListBinding
import com.leviancode.android.gsmbox.helpers.ItemDragHelperCallback
import com.leviancode.android.gsmbox.helpers.ItemDragListener
import com.leviancode.android.gsmbox.ui.extra.DeleteConfirmationDialog
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu
import com.leviancode.android.gsmbox.ui.templates.viewmodel.TemplateListViewModel
import com.leviancode.android.gsmbox.utils.*
import kotlinx.coroutines.launch

class TemplateListFragment : Fragment(), ItemDragListener {
    private lateinit var binding: FragmentTemplateListBinding
    private val viewModel: TemplateListViewModel by viewModels()
    private val args: TemplateListFragmentArgs by navArgs()
    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var listAdapter: TemplateListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_template_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        listAdapter = TemplateListAdapter(viewModel)
        binding.adapter = listAdapter
        binding.toolbarTemplateList.apply {
            title = args.groupName
            setNavigationOnClickListener { goBack() }
        }
        itemTouchHelper = ItemTouchHelper(ItemDragHelperCallback(this)).apply {
            attachToRecyclerView(binding.templatesRecyclerView)
        }

        observeUI()
    }

    private fun observeUI() {
        viewModel.getGroupTemplates(args.groupId).observe(viewLifecycleOwner){ list ->
            binding.tvListEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            binding.adapter?.submitList(list)
        }

        viewModel.createTemplateLiveEvent.observe(viewLifecycleOwner) {
            showEditableTemplateDialog(it)
        }

        viewModel.sendMessageLiveEvent.observe(viewLifecycleOwner) { sendMessage(it) }

        viewModel.popupMenuLiveEvent.observe(viewLifecycleOwner) { showPopup(it) }
    }

    private fun showPopup(pair: Pair<View, Template>) {
        ItemPopupMenu(requireContext(), pair.first).showSimple { result ->
            when (result) {
                EDIT -> showEditableTemplateDialog(pair.second)
                DELETE -> deleteTemplate(pair.second)
            }
        }
    }

    private fun deleteTemplate(item: Template) {
        DeleteConfirmationDialog(requireContext()).apply {
            title = getString(R.string.delete_template)
            message = getString(R.string.delete_template_confirmation)
            show { result ->
                if (result) viewModel.deleteTemplate(item)
            }
        }
    }

    private fun sendMessage(template: Template) {
        lifecycleScope.launch {
            SmsManager.sendSms(requireContext(), template)
        }
    }

    private fun showEditableTemplateDialog(template: Template) {
        navigate {
            TemplateListFragmentDirections.actionOpenEditableTemplate(
                template.apply { groupId = args.groupId }
            )
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        listAdapter.moveItems(fromPosition, toPosition)
    }

    override fun onMoveFinished() {
        viewModel.updateAll(listAdapter.currentList)
    }
}