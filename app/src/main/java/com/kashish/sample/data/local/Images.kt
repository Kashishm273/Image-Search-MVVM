package com.kashish.sample.data.local

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.kashish.sample.data.remote.Photo

@Entity(tableName = "Images")
data class Images (

    @PrimaryKey
    @ColumnInfo(name = "search_text")
    var searchText: String = String(),

    var images:MutableList<Photo>? = ArrayList()
)