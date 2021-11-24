package com.leviancode.android.gsmbox.ui.screens.recipients.viewpager

import android.view.View
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.FragmentRecipientsPagerBinding
import com.leviancode.android.gsmbox.ui.base.BaseFragment
import com.leviancode.android.gsmbox.ui.screens.recipients.groups.list.RecipientGroupListViewModel
import com.leviancode.android.gsmbox.ui.screens.recipients.recipients.list.RecipientListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipientsPagerFragment : BaseFragment<FragmentRecipientsPagerBinding>(R.layout.fragment_recipients_pager) {
    private val recipientViewModel: RecipientListViewModel by viewModel()
    private val groupViewModel: RecipientGroupListViewModel by viewModel()
    private var groupMode = false

    override var bottomNavViewVisibility: Int = View.VISIBLE

    override fun onCreated() {
        setupViewPager()
        observeEvents()
    }

    private fun observeEvents() {
        binding.fabRecipients.setOnClickListener {
            if (groupMode) groupViewModel.onAddGroupClick()
            else recipientViewModel.onAddRecipientClick()
        }
    }

    private fun setupViewPager() {
        binding.recipientsViewPager.adapter = RecipientsViewPagerAdapter(this)
        binding.recipientsViewPager.offscreenPageLimit = 2
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