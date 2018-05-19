package com.kashish.sample.ui.viewImages

import com.kashish.sample.utils.BasePresenterImpl
import com.kashish.sample.utils.Constants
import com.kashish.sample.webServices.RestClient
import com.kashish.sample.webServices.WebConstants
import com.kashish.sample.data.remote.CommonPojo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewImagesPresenter : BasePresenterImpl<ViewImagesContract.View>(), ViewImagesContract.Presenter {

    override fun hitApiGetImages(showProgress: Boolean, pageNo: Int, searchText: String) {
        val map = HashMap<String, String>()
        map[WebConstants.METHOD] = Constants.API_METHOD
        map[WebConstants.API_KEY] = Constants.API_KEY
        map[WebConstants.NO_JSON_CALLBACK] = Constants.VALUE_1
        map[WebConstants.FORMAT] = Constants.JSON
        map[WebConstants.PER_PAGE] = Constants.VALUE_48
        map[WebConstants.TEXT] = searchText
        map[WebConstants.PAGE] = pageNo.toString()

        if (showProgress)
            getView()?.showLoading()

        RestClient.get()?.getImages(map)?.enqueue(object : Callback<CommonPojo> {

            override fun onResponse(call: Call<CommonPojo>?, response: Response<CommonPojo>) {
                if (showProgress)
                    getView()?.dismissLoading()
                if (response.isSuccessful) {
                    getView()?.setApiResponse(response.body()?.photos?.photo)
                } else {
                    getView()?.errorToast()
                }
            }

            override fun onFailure(call: Call<CommonPojo>?, t: Throwable?) {
                if (showProgress)
                    getView()?.dismissLoading()
                getView()?.errorToast()
            }

        })
    }

}