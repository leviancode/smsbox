package com.leviancode.android.gsmbox.ui.recipients.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.ui.recipients.adapters.RecipientsViewPagerAdapter
import com.leviancode.android.gsmbox.databinding.FragmentRecipientsPagerBinding
import com.leviancode.android.gsmbox.ui.recipients.view.groups.list.RecipientGroupListViewModel
import com.leviancode.android.gsmbox.ui.recipients.view.recipients.list.RecipientListViewModel

class RecipientsPagerFragment : Fragment() {
    private lateinit var binding: FragmentRecipientsPagerBinding
    private val recipientViewModel: RecipientListViewModel by viewModels()
    private val groupViewModel: RecipientGroupListViewModel by viewModels()
    private var groupMode = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_recipients_pager, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setListeners()
    }

    private fun setListeners() {
        binding.fabRecipients.setOnClickListener {
            if (groupMode) groupViewModel.onAddGroupClick()
            else recipientViewModel.onAddRecipientClick()
        }
    }

    private fun setupViewPager() {
        binding.recipientsViewPager.adapter = RecipientsViewPagerAdapter(this)
        TabLayoutMediator(
            binding.tabLayoutRecipient,
            binding.recipientsViewPager
        ) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.all)
                else -> getString(R.string.groups)
            }
        }.attach()

        binding.tabLayoutRecipient.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> groupMode = false
                    1 -> groupMode = true
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })
    }
}