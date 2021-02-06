package com.leviancode.android.gsmbox.ui.templates.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.leviancode.android.gsmbox.ui.templates.viewmodel.TemplateListViewModel
import com.leviancode.android.gsmbox.utils.SmsManager
import kotlinx.coroutines.launch

class TemplateListFragment : Fragment(){
    private lateinit var binding: FragmentTemplateListBinding
    private val viewModel: TemplateListViewModel by viewModels()
    private val args: TemplateListFragmentArgs by navArgs()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_template_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        binding.viewModel = viewModel
        binding.adapter = TemplateListAdapter(viewModel)

        binding.templatesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) {
                    binding.fabAddTemplate.show()
                }
                else if (dy > 0) {
                    binding.fabAddTemplate.hide()
                }
            }
        })

        observeUI()
    }

    private fun observeUI() {
        /*viewModel.templates.observe(viewLifecycleOwner){ list ->
            val templates = list.filter { it.groupId == args.groupId }
            binding.adapter?.submitList(templates)
        }*/

        viewModel.getGroupTemplates(args.groupId).observe(viewLifecycleOwner){
            binding.adapter?.submitList(it)
        }
        viewModel.createTemplateLiveEvent.observe(viewLifecycleOwner){
            showNewTemplateDialog()
        }

        viewModel.sendMessageLiveEvent.observe(viewLifecycleOwner){ sendMessage(it) }
    }

    private fun sendMessage(template: Template){
        lifecycleScope.launch {
            SmsManager.sendSms(requireContext(), template)
        }
    }

    private fun showNewTemplateDialog() {
        val action = TemplateListFragmentDirections.actionNewTemplate(args.groupId)
        navController.navigate(action)
    }

    private fun showEditDialog(template: Template) {
        Toast.makeText(requireContext(), "Template ${template.name} clicked!", Toast.LENGTH_SHORT).show()
    }

}