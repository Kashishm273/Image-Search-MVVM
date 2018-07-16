package com.kashish.imageSearch.ui.viewImages

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import com.kashish.imageSearch.data.ImagesRepository
import com.kashish.imageSearch.data.pojo.Photo
import com.kashish.imageSearch.utils.Resource

class ImagesViewModel : ViewModel(){

    var images = MediatorLiveData<Resource<ArrayList<Photo>?>>()
    private val imagesRepository = ImagesRepository()

    fun getImages(pageNo: Int, searchText: String){
        images.addSource(imagesRepository.getImages(pageNo, searchText)) {
            images.postValue(it)
        }
    }
}