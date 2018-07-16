package com.kashish.imageSearch.data.pojo

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class CommonPojo (

        @SerializedName("photos")
        @Expose
        var photos: Photos? = null,
        @SerializedName("stat")
        @Expose
        var stat: String? = null

)