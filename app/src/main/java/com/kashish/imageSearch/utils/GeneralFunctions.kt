package com.kashish.imageSearch.utils

import android.content.Context
import android.net.ConnectivityManager

object GeneralFunctions {

    fun isConnectedToNetwork(context: Context): Boolean {
        return try {
            val mConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            mNetworkInfo != null
        } catch (e: NullPointerException) {
            false
        }
    }
}
