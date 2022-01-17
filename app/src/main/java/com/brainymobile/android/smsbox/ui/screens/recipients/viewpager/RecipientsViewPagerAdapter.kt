package com.brainymobile.android.smsbox.ui.screens.recipients.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.brainymobile.android.smsbox.ui.screens.recipients.groups.list.RecipientGroupListFragment
import com.brainymobile.android.smsbox.ui.screens.recipients.recipients.list.RecipientListFragment

class RecipientsViewPagerAdapter(parentFragment: Fragment) : FragmentStateAdapter(parentFragment)  {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> RecipientListFragment()
            else -> RecipientGroupListFragment()
        }
    }
}