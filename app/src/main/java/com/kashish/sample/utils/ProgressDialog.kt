package com.kashish.sample.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import com.kashish.sample.R

class ProgressDialog(context: Context) {

    private var mDialog: Dialog? = null

    init {
        try {
            val li = LayoutInflater.from(context)
            val dialogView = li.inflate(R.layout.progress, null, false)

            mDialog = Dialog(context, R.style.NewDialog)
            mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            mDialog!!.setContentView(dialogView)
            mDialog!!.setCancelable(false)
        } catch (e: Exception) {
        }

    }

    fun show() {
        try {
            if (!mDialog!!.isShowing)
                mDialog!!.show()
        } catch (e: Exception) {

        }

    }

    fun dismiss() {
        try {
            mDialog!!.dismiss()
        } catch (e: Exception) {
        }

    }
}