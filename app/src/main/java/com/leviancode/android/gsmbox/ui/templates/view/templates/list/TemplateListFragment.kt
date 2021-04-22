package com.leviancode.android.gsmbox.ui.templates.view.templates.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.data.model.templates.Template
import com.leviancode.android.gsmbox.databinding.FragmentTemplateListBinding
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu.Companion.DELETE
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu.Companion.EDIT
import com.leviancode.android.gsmbox.ui.extra.alertdialogs.DeleteConfirmationAlertDialog
import com.leviancode.android.gsmbox.ui.templates.adapters.TemplateListAdapter
import com.leviancode.android.gsmbox.utils.extensions.goBack
import com.leviancode.android.gsmbox.utils.extensions.navigate
import com.leviancode.android.gsmbox.utils.helpers.ItemDragHelperCallback
import com.leviancode.android.gsmbox.utils.helpers.ItemDragListener

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
        listAdapter = TemplateListAdapter(viewModel)
        binding.viewModel = viewModel
        binding.adapter = listAdapter
        binding.toolbar.title = args.groupName
        observeUI()
    }

    private fun observeUI() {
        binding.templatesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private val fab = binding.fabAddTemplate
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && fab.isShown) fab.hide()
                else if (dy < 0 && !fab.isShown) fab.show()
            }
        })
        binding.toolbar.setNavigationOnClickListener { goBack() }
        itemTouchHelper = ItemTouchHelper(ItemDragHelperCallback(this)).apply {
            attachToRecyclerView(binding.templatesRecyclerView)
        }

        viewModel.loadTemplatesWithRecipients(args.groupId).observe(viewLifecycleOwner){ items ->
            binding.tvListEmpty.visibility =
                if (items.isEmpty()) View.VISIBLE
                else View.GONE
            listAdapter.submitList(items)
        }

        viewModel.createTemplateLiveEvent.observe(viewLifecycleOwner) {
            showEditableTemplateDialog(0, args.groupId)
        }

        viewModel.popupMenuLiveEvent.observe(viewLifecycleOwner) {
            showPopup(it.first, it.second)
        }
    }

    private fun showPopup(view: View, template: Template) {
        ItemPopupMenu(requireContext(), view).showEditDelete { result ->
            when (result) {
                EDIT -> showEditableTemplateDialog(template.templateId, args.groupId)
                DELETE -> deleteTemplate(template)
            }
        }
    }

    private fun deleteTemplate(item: Template) {
        DeleteConfirmationAlertDialog.show(
            requireContext(),
            getString(R.string.delete_template),
            getString(R.string.delete_template_confirmation)
        ) { confirmed ->
            if (confirmed) viewModel.deleteTemplate(item)
        }
    }


    private fun showEditableTemplateDialog(templateId: Int, groupId: Int) {
        navigate {
            TemplateListFragmentDirections.actionOpenEditableTemplate(
                templateId = templateId,
                groupId = groupId)
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        listAdapter.moveItems(fromPosition, toPosition)
    }

    override fun onMoveFinished() {
        viewModel.updateOrder(listAdapter.currentList)
    }
}