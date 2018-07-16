package com.kashish.imageSearch.ui.viewSingleImage

import android.graphics.Bitmap
import android.os.Build
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.kashish.imageSearch.R
import com.kashish.imageSearch.data.pojo.Photo
import com.kashish.imageSearch.utils.Constants
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {

    private var mStartingPosition: Int? = 0
    private var mAlbumPosition: Int? = 0
    private var photo: Photo? = null

    companion object {

        fun newInstance(position: Int, startingPosition: Int, photo: Photo?): DetailsFragment {
            val args = Bundle()
            args.putInt(Constants.IMAGE_POSITION, position)
            args.putInt(Constants.START_ITEM_POSITION, startingPosition)
            args.putParcelable(Constants.PHOTO, photo)
            val fragment = DetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mStartingPosition = arguments?.getInt(Constants.START_ITEM_POSITION)
        mAlbumPosition = arguments?.getInt(Constants.IMAGE_POSITION)
        photo = arguments?.getParcelable(Constants.PHOTO)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val albumImageUrl = "https://farm${photo?.farm}.staticflickr.com/${photo?.server}" +
                "/${photo?.id}_${photo?.secret}_n.jpg"

        ivImage.transitionName = mAlbumPosition.toString()
        activity?.let {
            Glide.with(it).asBitmap().load(albumImageUrl).listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                    activity?.startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    activity?.startPostponedEnterTransition()
                    return false
                }

            }).into(ivImage)
        }
    }


}