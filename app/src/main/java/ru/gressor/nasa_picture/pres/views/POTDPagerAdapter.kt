package ru.gressor.nasa_picture.pres.views

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.gressor.nasa_picture.R
import ru.gressor.nasa_picture.pres.App

class POTDPagerAdapter(
    fragmentManager: FragmentManager
): FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = PAGES_QTTY

    override fun getItem(position: Int): Fragment = POTDFragment.newInstance(position)

    override fun getPageTitle(position: Int): CharSequence = when(position) {
        0 -> App.instance.getString(R.string.today)
        1 -> App.instance.getString(R.string.yesterday)
        else -> POTDFragment.getDate(position)
    }

    companion object {
        private const val PAGES_QTTY = 3
    }
}