package com.kashish.imageSearch.webServices

import com.kashish.imageSearch.utils.Constants

import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestClient {
    private var REST_CLIENT: RestApi? = null

    private val okHttpClient: OkHttpClient
        get() {
            val okClient = OkHttpClient.Builder()
            okClient.connectTimeout(60, TimeUnit.SECONDS)
            return okClient.build()
        }

    init {
        setUpRestClient()
    }

    fun get(): RestApi? {
        return REST_CLIENT
    }

    private fun setUpRestClient() {
        val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient).build()
        REST_CLIENT = retrofit.create(RestApi::class.java)
    }
}
