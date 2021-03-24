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
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.TemplateListAdapter
import com.leviancode.android.gsmbox.data.model.templates.Template
import com.leviancode.android.gsmbox.databinding.FragmentTemplateListBinding
import com.leviancode.android.gsmbox.helpers.ItemDragHelperCallback
import com.leviancode.android.gsmbox.helpers.ItemDragListener
import com.leviancode.android.gsmbox.ui.extra.alertdialogs.DeleteConfirmationAlertDialog
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu.Companion.DELETE
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu.Companion.EDIT
import com.leviancode.android.gsmbox.ui.templates.viewmodel.TemplateListViewModel
import com.leviancode.android.gsmbox.utils.extensions.goBack
import com.leviancode.android.gsmbox.utils.extensions.navigate
import com.leviancode.android.gsmbox.utils.managers.SmsManager
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
        listAdapter = TemplateListAdapter(viewModel)
        binding.viewModel = viewModel
        binding.adapter = listAdapter

        observeUI()
    }

    private fun observeUI() {
        binding.templatesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            private val fab = binding.fabAddTemplate
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy >0) {
                    // Scroll Down
                    if (fab.isShown) {
                        fab.hide()
                    }
                }
                else if (dy <0) {
                    // Scroll Up
                    if (!fab.isShown) {
                        fab.show()
                    }
                }
            }
        })
        binding.toolbar.setNavigationOnClickListener { goBack() }
        itemTouchHelper = ItemTouchHelper(ItemDragHelperCallback(this)).apply {
            attachToRecyclerView(binding.templatesRecyclerView)
        }
        
        viewModel.getGroupWithTemplates(args.groupId).observe(viewLifecycleOwner) { group ->

            binding.toolbar.title = group.group.getName()
            binding.tvListEmpty.visibility =
                if (group.templates.isEmpty()) View.VISIBLE
                else View.GONE
            listAdapter.submitList(group.templates)
        }

        viewModel.createTemplateLiveEvent.observe(viewLifecycleOwner) {
            showEditableTemplateDialog(it)
        }

        viewModel.sendMessageLiveEvent.observe(viewLifecycleOwner) { sendMessage(it) }

        viewModel.popupMenuLiveEvent.observe(viewLifecycleOwner) { showPopup(it) }
    }

    private fun showPopup(pair: Pair<View, Template>) {
        ItemPopupMenu(requireContext(), pair.first).showEditDelete { result ->
            when (result) {
                EDIT -> showEditableTemplateDialog(pair.second)
                DELETE -> deleteTemplate(pair.second)
            }
        }
    }

    private fun deleteTemplate(item: Template) {
        DeleteConfirmationAlertDialog(requireContext()).apply {
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
                template.apply { templateGroupId = args.groupId }
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