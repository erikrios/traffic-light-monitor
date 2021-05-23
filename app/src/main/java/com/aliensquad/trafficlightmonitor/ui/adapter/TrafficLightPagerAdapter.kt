package com.aliensquad.trafficlightmonitor.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TrafficLightPagerAdapter(
    parentFragment: Fragment,
    private val fragments: List<Fragment>
) : FragmentStateAdapter(parentFragment) {
    override fun createFragment(position: Int): Fragment = fragments[position]

    override fun getItemCount(): Int = fragments.size
}