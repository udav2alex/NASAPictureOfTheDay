package ru.gressor.nasa_picture.pres.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import ru.gressor.nasa_picture.R

class PagerContainerFragment: Fragment(R.layout.fragment_pager_container) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vpPOTDContainer: ViewPager = view.findViewById(R.id.vp_potd_container)
        vpPOTDContainer.adapter = POTDPagerAdapter(childFragmentManager)

        val tlPODTTabs: TabLayout = view.findViewById(R.id.tl_potd_tabs)
        tlPODTTabs.setupWithViewPager(vpPOTDContainer)
    }
}