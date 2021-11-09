package dev.bahodir.networkentrancelessonmedium.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.bahodir.networkentrancelessonmedium.fragments.VPLikeFragment
import dev.bahodir.networkentrancelessonmedium.fragments.VPMainFragment

class VP(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0)
            VPMainFragment()
        else
            VPLikeFragment()
    }
}