package com.leviancode.android.gsmbox.ui.templates.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.TemplateListAdapter
import com.leviancode.android.gsmbox.data.model.Template
import com.leviancode.android.gsmbox.databinding.FragmentTemplateListBinding
import com.leviancode.android.gsmbox.ui.extra.DeleteConfirmationDialog
import com.leviancode.android.gsmbox.ui.extra.ItemPopupMenu
import com.leviancode.android.gsmbox.ui.templates.viewmodel.TemplateListViewModel
import com.leviancode.android.gsmbox.utils.DELETE
import com.leviancode.android.gsmbox.utils.EDIT
import com.leviancode.android.gsmbox.utils.SmsManager
import kotlinx.coroutines.launch

class TemplateListFragment : Fragment() {
    private lateinit var binding: FragmentTemplateListBinding
    private val viewModel: TemplateListViewModel by viewModels()
    private val args: TemplateListFragmentArgs by navArgs()
    private lateinit var navController: NavController

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
        navController = findNavController()
        binding.viewModel = viewModel
        binding.adapter = TemplateListAdapter(viewModel)
        binding.toolbarTemplateList.apply {
            title = args.groupName
            setNavigationOnClickListener { navController.navigateUp() }
        }

        binding.templatesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) {
                    binding.fabAddTemplate.show()
                } else if (dy > 0) {
                    binding.fabAddTemplate.hide()
                }
            }
        })

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
        val action =
            TemplateListFragmentDirections.actionOpenEditableTemplate(
                template.apply { groupId = args.groupId }
            )
        navController.navigate(action)
    }

}