package com.kashish.sample.ui.viewSingleImage

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kashish.sample.R
import com.kashish.sample.utils.Constants
import kotlinx.android.synthetic.main.activity_view_single_image.*
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.SharedElementCallback
import android.support.v4.view.ViewPager
import android.view.View
import com.kashish.sample.data.remote.Photo
import kotlinx.android.synthetic.main.fragment_details.*

class ViewSingleImageActivity : AppCompatActivity() {

    private var photosList = ArrayList<Photo?>()
    private var currentPosition: Int = 0
    private var startingPosition: Int = 0
    private var isReturning: Boolean = false
    private lateinit var adapter: DetailsFragmentPagerAdapter

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_single_image)
        postponeEnterTransition()
        setEnterSharedElementCallback(mCallback)

        getStartingPosition()
        setViewPager()
    }

    private fun setViewPager() {
        adapter = DetailsFragmentPagerAdapter(supportFragmentManager, startingPosition, photosList)
        viewPager.adapter = adapter
        viewPager.currentItem = currentPosition
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                currentPosition = position
            }
        })
    }

    override fun finishAfterTransition() {
        isReturning = true
        val data = Intent()
        data.putExtra(Constants.START_ITEM_POSITION, startingPosition)
        data.putExtra(Constants.CURRENT_ITEM_POSITION, currentPosition)
        setResult(Activity.RESULT_OK, data)
        super.finishAfterTransition()
    }

    private fun getStartingPosition() {
        photosList = intent.getParcelableArrayListExtra(Constants.LIST)
        startingPosition = intent.getIntExtra(Constants.START_ITEM_POSITION, 0)
        currentPosition = startingPosition
    }

    private val mCallback = object : SharedElementCallback() {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onMapSharedElements(names: MutableList<String>, sharedElements: MutableMap<String, View>) {
            if (isReturning) {
                val sharedElement = adapter.mCurrentDetailsFragment.ivImage
                if (startingPosition != currentPosition) {
                    names.clear()
                    names.add(sharedElement.transitionName)
                    sharedElements.clear()
                    sharedElements[sharedElement.transitionName] = sharedElement
                }
            }
        }
    }
}
