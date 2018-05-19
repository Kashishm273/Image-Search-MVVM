package com.kashish.sample.ui.viewImages

import com.kashish.sample.utils.BasePresenter
import com.kashish.sample.utils.BaseView
import com.kashish.sample.data.remote.Photo

class ViewImagesContract {

    interface View : BaseView {
        fun setListeners()

        fun onSearchClick()

        fun errorToast()

        fun showLoading()

        fun dismissLoading()

        fun setApiResponse(photo: ArrayList<Photo>?)
    }

    interface Presenter : BasePresenter<View> {
        fun hitApiGetImages(showProgress : Boolean, pageNo: Int, searchText: String)
    }


}