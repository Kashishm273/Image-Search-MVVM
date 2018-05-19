package com.kashish.sample.ui.viewImages

import android.app.Activity
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
import com.kashish.sample.R
import com.kashish.sample.utils.ProgressDialog
import com.kashish.sample.data.remote.Photo
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.kashish.sample.data.local.AppDatabase
import com.kashish.sample.data.local.Images
import com.kashish.sample.ui.searchImages.SearchActivity
import com.kashish.sample.utils.Constants
import com.kashish.sample.utils.GeneralFunctions
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.SharedElementCallback
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.kashish.sample.ui.viewSingleImage.ViewSingleImageActivity
import kotlinx.android.synthetic.main.item_image.view.*

class MainActivity : AppCompatActivity(), ViewImagesContract.View, View.OnClickListener,
        ImagesAdapter.Interface {

    private var spanCount: Int = 2
    private var tmpRenterState: Bundle? = null
    private var presenter = ViewImagesPresenter()
    private var photosList = ArrayList<Photo?>()
    private var backPressed: Long = 0
    private var roomList = ArrayList<Photo>()
    private var dbList: MutableList<Images>? = ArrayList()
    private lateinit var progressDialog: ProgressDialog
    private lateinit var adapter: ImagesAdapter
    private var searchText = ""
    private var isLoading = false
    private var pageNo = 1
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setExitSharedElementCallback(mCallback)
        init()
        setAdapter()
        setListeners()
        setScrollListener()
        onSearchClick()
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

    override fun showLoading() {
        progressDialog.show()
    }

    override fun dismissLoading() {
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
        searchText = getString(R.string.android)
        tvSearch.text = searchText
        presenter.attachView(this)
        progressDialog = ProgressDialog(this)
        database = AppDatabase.getInstance(this)
    }

    override fun setListeners() {
        rlSearch.setOnClickListener(this)
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
                        presenter.hitApiGetImages(false, ++pageNo, searchText)
                    }
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rlSearch -> {
                onSearchClick()
            }
        }
    }

    override fun setApiResponse(photo: ArrayList<Photo>?) {
        pbLoader.visibility = View.GONE
        isLoading = false
        photosList.addAll(photo.orEmpty())
        roomList.addAll(photo.orEmpty())
        val size = photo?.size
        if (size != null && size > 0) {
            //Adding images to the room db
            val images = Images(searchText, roomList)
            database.userDao().insertAll(images)
        } else
            Toast.makeText(this, getString(R.string.no_images_found), Toast.LENGTH_SHORT).show()
        adapter.notifyDataSetChanged()
    }

    override fun errorToast() {
        pbLoader.visibility = View.GONE
        isLoading = false
        Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
    }

    override fun onSearchClick() {
        val intent = Intent(this, SearchActivity::class.java)
        startActivityForResult(intent, Constants.REQUEST_CODE)
    }

    private fun checkDataInDB() {
        dbList = database.userDao().loadAllByIds(searchText)
        if (dbList?.size == 0)
            Toast.makeText(this, getString(R.string.no_images), Toast.LENGTH_SHORT).show()
        else {
            dbList?.get(0)?.images?.let { photosList.addAll(it) }
            //to continue adding data from start when offline
            dbList?.get(0)?.images?.let { roomList.addAll(it) }
            adapter.notifyDataSetChanged()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            searchText = data?.getStringExtra(Constants.SEARCH_TEXT).toString()
            tvSearch.text = searchText
            photosList.clear()
            roomList.clear()
            adapter.notifyDataSetChanged()
            if (GeneralFunctions.isConnectedToNetwork(this)) {
                pageNo = 1
                presenter.hitApiGetImages(true, pageNo, searchText)
            } else
                checkDataInDB()
        }
    }

    override fun onImageClick(position: Int, ivImage: ImageView) {
        val intent = Intent(this, ViewSingleImageActivity::class.java)
        intent.putExtra(Constants.START_ITEM_POSITION, position)
        intent.putParcelableArrayListExtra(Constants.LIST, photosList)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                ivImage,
                position.toString())
        startActivity(intent, options.toBundle())
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
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
