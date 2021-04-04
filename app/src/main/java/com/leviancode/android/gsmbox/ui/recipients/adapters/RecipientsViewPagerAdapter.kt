package com.leviancode.android.gsmbox.ui.recipients.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.leviancode.android.gsmbox.ui.recipients.view.groups.list.RecipientGroupListFragment
import com.leviancode.android.gsmbox.ui.recipients.view.recipients.list.RecipientListFragment

class RecipientsViewPagerAdapter(parentFragment: Fragment) : FragmentStateAdapter(parentFragment)  {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> RecipientListFragment()
            else -> RecipientGroupListFragment()
        }
    }
}