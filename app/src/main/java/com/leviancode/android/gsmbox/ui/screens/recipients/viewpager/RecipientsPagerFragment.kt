package com.leviancode.android.gsmbox.ui.screens.recipients.viewpager

import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.FragmentRecipientsPagerBinding
import com.leviancode.android.gsmbox.ui.base.BaseFragment
import com.leviancode.android.gsmbox.ui.screens.recipients.groups.list.RecipientGroupListViewModel
import com.leviancode.android.gsmbox.ui.screens.recipients.recipients.list.RecipientListViewModel
import com.leviancode.android.gsmbox.utils.extensions.onTabSelected
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
            if (groupMode) {
                binding.fabRecipients.hide()
                groupViewModel.onAddGroupClick()
            }
            else recipientViewModel.onAddRecipientClick()
        }
        groupViewModel.createGroupDialogDismissed.observe(viewLifecycleOwner){
            binding.fabRecipients.show()
        }
    }

    private fun setupViewPager() {
        binding.recipientsViewPager.apply {
            adapter = RecipientsViewPagerAdapter(this@RecipientsPagerFragment)
            offscreenPageLimit = 2
            TabLayoutMediator(
                binding.tabLayoutRecipient,
                this
            ) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.all)
                    else -> getString(R.string.groups)
                }
            }.attach()
        }

        binding.tabLayoutRecipient.onTabSelected { tab ->
            groupMode = tab.position == 1
            binding.fabRecipients.show()
        }
    }
}