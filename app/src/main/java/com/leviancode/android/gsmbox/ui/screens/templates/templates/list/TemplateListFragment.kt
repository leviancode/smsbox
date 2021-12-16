package com.leviancode.android.gsmbox.ui.screens.templates.templates.list

import android.view.View
import androidx.navigation.fragment.navArgs
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.FragmentTemplateListBinding
import com.leviancode.android.gsmbox.databinding.ListItemTemplateBinding
import com.leviancode.android.gsmbox.ui.base.BaseFragment
import com.leviancode.android.gsmbox.ui.base.BaseListAdapter
import com.leviancode.android.gsmbox.ui.dialogs.PopupMenus
import com.leviancode.android.gsmbox.ui.dialogs.PopupMenus.MenuItem.DELETE
import com.leviancode.android.gsmbox.ui.dialogs.PopupMenus.MenuItem.EDIT
import com.leviancode.android.gsmbox.ui.dialogs.alertdialogs.DeleteConfirmationAlertDialog
import com.leviancode.android.gsmbox.ui.entities.templates.TemplateUI
import com.leviancode.android.gsmbox.utils.extensions.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TemplateListFragment : BaseFragment<FragmentTemplateListBinding>(R.layout.fragment_template_list) {
    private val viewModel: TemplateListViewModel by viewModel()
    private val args: TemplateListFragmentArgs by navArgs()
    private val listAdapter =
        BaseListAdapter<TemplateUI, ListItemTemplateBinding>(R.layout.list_item_template) { binding, item, position ->
            binding.model = item
            binding.viewModel = viewModel
        }

    override fun onCreated() {
        binding.viewModel = viewModel
        binding.recyclerView.adapter = listAdapter
        updateTitle(args.groupName)
        observeEvents()
        observeData()
    }

    override fun onPause() {
        super.onPause()
        binding.fabAddTemplate.hide()
    }

    override fun onResume() {
        super.onResume()
        binding.fabAddTemplate.show()
    }

    private fun updateTitle(title: String) {
        binding.toolbar.title = title
    }

    private fun observeEvents() {
        binding.recyclerView.hideFabWhileScrolling(binding.fabAddTemplate)
        binding.toolbar.setNavigationOnClickListener { navigateBack() }

        viewModel.createTemplateEvent.observe(viewLifecycleOwner) {
            showEditableTemplateDialog(0, args.groupId)
        }

        viewModel.popupMenuEvent.observe(viewLifecycleOwner) {
            showPopup(it.first, it.second)
        }
    }

    private fun observeData() {
        viewModel.getTemplates(args.groupId).observe(viewLifecycleOwner) { items ->
            binding.tvListEmpty.visibility(items.isEmpty())
            listAdapter.submitList(items)
        }
    }

    private fun showPopup(view: View, template: TemplateUI) {
        PopupMenus(view).showEditDelete { result ->
            when (result) {
                EDIT -> showEditableTemplateDialog(template.id, args.groupId)
                DELETE -> deleteTemplate(template)
                else -> {}
            }
        }
    }

    private fun deleteTemplate(item: TemplateUI) {
        DeleteConfirmationAlertDialog(requireContext()).show(
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
                groupId = groupId
            )
        }
    }
}