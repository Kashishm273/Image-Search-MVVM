package com.kashish.imageSearch.data

import android.arch.lifecycle.MutableLiveData
import retrofit2.Callback
import com.kashish.imageSearch.data.pojo.CommonPojo
import com.kashish.imageSearch.data.pojo.Photo
import com.kashish.imageSearch.utils.Constants
import com.kashish.imageSearch.utils.Resource
import com.kashish.imageSearch.webServices.RestClient
import com.kashish.imageSearch.webServices.WebConstants
import retrofit2.Call
import retrofit2.Response
import kotlin.collections.HashMap

class ImagesRepository {

    fun getImages(pageNo: Int, searchText: String): MutableLiveData<Resource<ArrayList<Photo>?>> {

        val resource = MutableLiveData<Resource<ArrayList<Photo>?>>()

        val map = HashMap<String, String>()
        map[WebConstants.METHOD] = Constants.API_METHOD
        map[WebConstants.API_KEY] = Constants.API_KEY
        map[WebConstants.NO_JSON_CALLBACK] = Constants.VALUE_1
        map[WebConstants.FORMAT] = Constants.JSON
        map[WebConstants.PER_PAGE] = Constants.VALUE_48
        map[WebConstants.TEXT] = searchText
        map[WebConstants.PAGE] = pageNo.toString()

        RestClient.get()?.getImages(map)?.enqueue(object : Callback<CommonPojo> {

            override fun onResponse(call: Call<CommonPojo>?, response: Response<CommonPojo>) {
                if (response.isSuccessful) {
                    resource.postValue(Resource.success(response.body()?.photos?.photo))
                } else {
                    resource.postValue(Resource.error())
                }
            }

            override fun onFailure(call: Call<CommonPojo>?, t: Throwable?) {
                resource.postValue(Resource.failure())
            }
        })

        return resource
    }
}