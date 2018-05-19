package com.kashish.sample.webServices

import com.kashish.sample.data.remote.CommonPojo

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RestApi {

    @GET("rest/")
    fun getImages(@QueryMap map: Map<String, String>): Call<CommonPojo>
}
