package com.kashish.imageSearch.ui.viewImages

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.item_image.view.*
import com.bumptech.glide.Glide
import com.kashish.imageSearch.R
import com.kashish.imageSearch.data.pojo.Photo
import com.bumptech.glide.request.RequestOptions

class ImagesAdapter(private val context: Context, private val photosList: ArrayList<Photo?>,
                    private val imagesInterface: Interface) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var flickrLink = "staticflickr.com"
    private var initials = "https://farm"
    private var qualityFormat = "n.jpg"

    override fun getItemCount(): Int {
        return photosList.size
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.transitionName = position.toString()
        val requestOptions = RequestOptions()
        requestOptions.placeholder(R.color.colorGray)
        val url = "$initials${photosList[position]?.farm}.$flickrLink/${photosList[position]?.server}" +
                "/${photosList[position]?.id}_${photosList[position]?.secret}_$qualityFormat"
        Glide.with(context).setDefaultRequestOptions(requestOptions).asBitmap().load(url)
                .into(holder.itemView?.ivImage!!)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image, parent,
                false))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.ivImage.transitionName = adapterPosition.toString()
            itemView.ivImage.setOnClickListener {
                imagesInterface.onImageClick(adapterPosition, itemView.ivImage)
            }
        }
    }

    interface Interface {
        fun onImageClick(position: Int, ivImage: ImageView)
    }
}