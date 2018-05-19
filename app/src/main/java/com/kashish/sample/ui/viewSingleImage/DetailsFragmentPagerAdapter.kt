package com.kashish.sample.ui.viewSingleImage

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.ViewGroup
import com.kashish.sample.data.remote.Photo

class DetailsFragmentPagerAdapter(fm: FragmentManager, private val startingPosition: Int,
                                  private val photosList: ArrayList<Photo?>) :
        FragmentStatePagerAdapter(fm) {

    lateinit var mCurrentDetailsFragment : DetailsFragment

    override fun getItem(position: Int): Fragment {
        return DetailsFragment.newInstance(position, startingPosition, photosList[position])
    }

    override fun getCount(): Int {
        return photosList.size
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
        mCurrentDetailsFragment = `object` as DetailsFragment
    }
}