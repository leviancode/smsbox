package com.leviancode.android.gsmbox.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.leviancode.android.gsmbox.ui.recipients.view.fragment.RecipientGroupListFragment
import com.leviancode.android.gsmbox.ui.recipients.view.fragment.RecipientListFragment

class RecipientsViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa)  {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> RecipientListFragment()
            else -> RecipientGroupListFragment()
        }
    }
}