package com.leviancode.android.gsmbox.ui.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.ListItemClickListener
import com.leviancode.android.gsmbox.adapters.TemplateListAdapter
import com.leviancode.android.gsmbox.data.model.Template
import com.leviancode.android.gsmbox.databinding.FragmentTemplateListBinding
import com.leviancode.android.gsmbox.ui.view.dialog.NewTemplateDialog
import com.leviancode.android.gsmbox.ui.viewmodel.TemplatesViewModel
import com.leviancode.android.gsmbox.utils.ARG_GROUP_ID

class TemplateListFragment : Fragment(){
    private lateinit var binding: FragmentTemplateListBinding
    private val viewModel: TemplatesViewModel by activityViewModels()
    private var groupId = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_template_list, container, false)
        arguments?.getString(ARG_GROUP_ID)?.let { groupId = it }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.adapter = TemplateListAdapter().apply {
            clickListener = ListItemClickListener { showEditDialog(it) }
        }

        binding.fabAddTemplate.setOnClickListener{
            showNewTemplateDialog()
        }

        observeUI()
    }

    private fun observeUI() {
        viewModel.templates.observe(viewLifecycleOwner){ list ->
            val templates = list.filter { it.groupId == groupId }
            binding.adapter?.submitList(templates)
        }
    }

    private fun showNewTemplateDialog() {
        NewTemplateDialog.display(requireActivity().supportFragmentManager, groupId)
    }

    private fun showEditDialog(template: Template) {
        Toast.makeText(requireContext(), "Template ${template.name} clicked!", Toast.LENGTH_SHORT).show()
    }


}