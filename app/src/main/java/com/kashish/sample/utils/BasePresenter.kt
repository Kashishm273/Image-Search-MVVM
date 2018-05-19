package com.kashish.sample.utils

interface BasePresenter<in V : BaseView> {
    fun attachView(view: V)

    fun detachView()
}