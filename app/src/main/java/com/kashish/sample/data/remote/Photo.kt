package com.kashish.sample.data.remote

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Photo(

        @SerializedName("id")
        @Expose
        var id: String? = null,
        @SerializedName("secret")
        @Expose
        var secret: String? = null,
        @SerializedName("server")
        @Expose
        var server: String? = null,
        @SerializedName("farm")
        @Expose
        var farm: Int? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(secret)
        parcel.writeString(server)
        parcel.writeValue(farm)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Photo> {
        override fun createFromParcel(parcel: Parcel): Photo {
            return Photo(parcel)
        }

        override fun newArray(size: Int): Array<Photo?> {
            return arrayOfNulls(size)
        }
    }
}