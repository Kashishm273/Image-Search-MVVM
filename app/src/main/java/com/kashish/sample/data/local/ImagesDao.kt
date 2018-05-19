package com.kashish.sample.data.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface ImagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(images: Images)

    @Query("SELECT * FROM Images WHERE search_text LIKE :searchText")
    fun loadAllByIds(searchText: String): MutableList<Images>?

    @Query("SELECT search_text FROM Images")
    fun loadSearchText(): MutableList<String>
}