package com.kashish.sample.data.local

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kashish.sample.data.remote.Photo

class Converters {

    @TypeConverter
    fun fromString(value: String): MutableList<Photo>? {
        val listType = object : TypeToken<MutableList<Photo>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: MutableList<Photo>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}