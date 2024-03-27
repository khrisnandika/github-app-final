package com.example.githubmobile.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubmobile.ui.FollowFragment

class SectionPagerAdapter(activity: FragmentActivity, private val fragment: MutableList<Fragment>) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int  = fragment.size

    override fun createFragment(position: Int): Fragment {

        return fragment[position].apply {
            arguments = Bundle().apply {
                putInt(FollowFragment.ARG_SECTION_NUMBER, position + 1)
            }
        }
    }

}