package com.kashish.imageSearch.webServices

import com.kashish.imageSearch.data.pojo.CommonPojo

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RestApi {

    @GET("rest/")
    fun getImages(@QueryMap map: Map<String, String>): Call<CommonPojo>
}
