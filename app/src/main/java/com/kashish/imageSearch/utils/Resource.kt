package com.kashish.imageSearch.utils


data class Resource<T>(var status: Status?, var data: T?, var message: String?) {


    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(): Resource<T> {
            return Resource(Status.ERROR, null, null)
        }

        fun <T> failure(): Resource<T> {
            return Resource(Status.FAILURE, null, null)
        }
    }
}