package com.kashish.imageSearch.ui.viewImages

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.kashish.imageSearch.R
import com.kashish.imageSearch.data.pojo.Photo
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.SharedElementCallback
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.kashish.imageSearch.ui.viewSingleImage.ViewSingleImageActivity
import com.kashish.imageSearch.utils.*
import kotlinx.android.synthetic.main.item_image.view.*

class ViewImagesActivity : AppCompatActivity(), View.OnClickListener,
        ImagesAdapter.Interface {

    private var spanCount: Int = 2
    private var tmpRenterState: Bundle? = null
    private var photosList = ArrayList<Photo?>()
    private var backPressed: Long = 0
    private lateinit var progressDialog: ProgressDialog
    private lateinit var adapter: ImagesAdapter
    private var searchText = ""
    private var isLoading = false
    private var pageNo = 1
    private lateinit var viewModel : ImagesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        //TODO handle the gridLayout count when screen is rotated
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setExitSharedElementCallback(mCallback)
        init()
        setListeners()
        setAdapter()
        setScrollListener()
    }

    private fun setAdapter() {
        rvImages.layoutManager = GridLayoutManager(this, 2)
        adapter = ImagesAdapter(this, photosList, this)
        rvImages.adapter = adapter
    }

    private fun setLayoutManager(count: Int) {
        if (spanCount != count) {
            spanCount = count
            rvImages.layoutManager = GridLayoutManager(this, count)
        }
    }

    private val mCallback = object : SharedElementCallback() {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>?) {
            if (tmpRenterState != null) {
                val startingPosition = tmpRenterState?.getInt(Constants.START_ITEM_POSITION)
                val currentPosition = tmpRenterState?.getInt(Constants.CURRENT_ITEM_POSITION)
                if (startingPosition != currentPosition) {
                    //updating the shared element
                    val newTransitionName = currentPosition.toString()
                    val newSharedElement = currentPosition?.let { rvImages.findViewHolderForAdapterPosition(it).itemView.ivImage }
                    if (newSharedElement != null) {
                        names?.clear()
                        names?.add(newTransitionName)
                        sharedElements!!.clear()
                        sharedElements[newTransitionName] = newSharedElement
                    }
                }
                tmpRenterState = null
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityReenter(requestCode: Int, data: Intent) {
        super.onActivityReenter(requestCode, data)
        tmpRenterState = Bundle(data.extras)
        val startingPosition = tmpRenterState?.getInt(Constants.START_ITEM_POSITION)
        val currentPosition = tmpRenterState?.getInt(Constants.CURRENT_ITEM_POSITION)
        if (startingPosition != currentPosition) {
            currentPosition?.let { rvImages.scrollToPosition(it) }
        }
        postponeEnterTransition()
        rvImages.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                rvImages.viewTreeObserver.removeOnPreDrawListener(this)
                rvImages.requestLayout()
                startPostponedEnterTransition()
                return true
            }
        })
    }

    private fun showLoading() {
        progressDialog.show()
    }

    private fun dismissLoading() {
        progressDialog.dismiss()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        menu?.findItem(R.id.count2)?.isChecked = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.count2 -> {
                item.isChecked = true
                setLayoutManager(2)
                return true
            }
            R.id.count3 -> {
                item.isChecked = true
                setLayoutManager(3)
                return true
            }
            R.id.count4 -> {
                item.isChecked = true
                setLayoutManager(4)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        progressDialog = ProgressDialog(this)
        viewModel = ViewModelProviders.of(this).get(ImagesViewModel::class.java)
        viewModel.images.observe(this, Observer<Resource<ArrayList<Photo>?>> { it ->
            dismissLoading()
            when(it?.status) {
                Status.SUCCESS -> setApiResponse(it.data)
                Status.ERROR, Status.FAILURE -> errorToast()
            }
        })
    }

    private fun setListeners() {
        ivSearch.setOnClickListener(this)
    }

    private fun setScrollListener() {
        rvImages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView!!.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (!isLoading && visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0 && totalItemCount >= 48) {
                    if (GeneralFunctions.isConnectedToNetwork(baseContext)) {
                        pbLoader.visibility = View.VISIBLE
                        isLoading = true
                        viewModel.getImages(++pageNo, searchText)
                    }
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivSearch -> {
                onSearchClick()
            }
        }
    }

    private fun setApiResponse(photo: ArrayList<Photo>?) {
        pbLoader.visibility = View.GONE
        isLoading = false
        photosList.addAll(photo.orEmpty())
        adapter.notifyDataSetChanged()
    }

    private fun errorToast() {
        pbLoader.visibility = View.GONE
        isLoading = false
        Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
    }

    private fun onSearchClick() {
        if (!etSearch.text.toString().trim().isEmpty()) {
            showLoading()
            searchText = etSearch.text.toString().trim()
            photosList.clear()
            adapter.notifyDataSetChanged()
            if (GeneralFunctions.isConnectedToNetwork(this)) {
                pageNo = 1
                viewModel.getImages(++pageNo, searchText)
            } else
                Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
        }
    }


    override fun onImageClick(position: Int, ivImage: ImageView) {
        val intent = Intent(this, ViewSingleImageActivity::class.java)
        intent.putExtra(Constants.START_ITEM_POSITION, position)
        intent.putParcelableArrayListExtra(Constants.LIST, photosList)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                ivImage, position.toString())
        startActivity(intent, options.toBundle())
    }

    override fun onBackPressed() {
        if (backPressed + Constants.BACK_DURATION > System.currentTimeMillis()) {
            this.finishAffinity()
        } else {
            Toast.makeText(this, getString(R.string.press_back), Toast.LENGTH_SHORT).show()
            backPressed = System.currentTimeMillis()
        }
    }
}
