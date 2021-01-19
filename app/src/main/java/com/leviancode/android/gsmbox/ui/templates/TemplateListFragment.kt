package com.leviancode.android.gsmbox.ui.templates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.adapters.TemplateListAdapter
import com.leviancode.android.gsmbox.databinding.FragmentListTemplateBinding
import com.leviancode.android.gsmbox.model.TemplateGroup

class TemplateListFragment : Fragment() {
    private lateinit var fragmentBinding: FragmentListTemplateBinding
    private val viewModel by viewModels<TemplateListViewModel>()
    private lateinit var recyclerView: RecyclerView
    private var listAdapter: TemplateListAdapter? = null
    //private val groupItems = mutableListOf<TemplateGroup>()
    private val groupItems = ObservableArrayList<TemplateGroup>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_template, container, false)
        fragmentBinding.lifecycleOwner = this
        fragmentBinding.adapter = TemplateListAdapter(groupItems)
        subscribeUi()
        return fragmentBinding.root
    }

    private fun subscribeUi(){
        viewModel.groups.observe(viewLifecycleOwner){ group ->
            groupItems.add(group)
        }
    }

    private fun setupAdapter() {
        if (isAdded || listAdapter == null) {
            listAdapter =  TemplateListAdapter(groupItems)
            recyclerView.adapter = listAdapter
        }
    }
}