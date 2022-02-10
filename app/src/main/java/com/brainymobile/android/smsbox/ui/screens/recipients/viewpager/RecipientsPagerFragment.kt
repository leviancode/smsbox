package com.brainymobile.android.smsbox.ui.screens.recipients.viewpager

import android.view.View
import androidx.fragment.app.viewModels
import com.brainymobile.android.smsbox.R
import com.brainymobile.android.smsbox.databinding.FragmentRecipientsPagerBinding
import com.brainymobile.android.smsbox.ui.base.BaseFragment
import com.brainymobile.android.smsbox.ui.screens.recipients.groups.list.RecipientGroupListViewModel
import com.brainymobile.android.smsbox.ui.screens.recipients.recipients.list.RecipientListViewModel
import com.brainymobile.android.smsbox.utils.extensions.onTabSelected
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipientsPagerFragment :
    BaseFragment<FragmentRecipientsPagerBinding>(R.layout.fragment_recipients_pager) {
    private val recipientViewModel: RecipientListViewModel by viewModels()
    private val groupViewModel: RecipientGroupListViewModel by viewModels()
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
            } else recipientViewModel.onAddRecipientClick()
        }
        groupViewModel.createGroupDialogDismissed.observe(viewLifecycleOwner) {
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