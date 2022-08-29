package com.example.mydiary.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.example.mydiary.R


object ProgressDialogUtil {
    private var mAlertDialog: AlertDialog? = null

    fun createprogressdialog(context: Context) {
        mAlertDialog = AlertDialog.Builder(context, R.style.CustomProgressDialog).create()

        val loadView: View =
            LayoutInflater.from(context).inflate(R.layout.progress_dialog, null)
        mAlertDialog!!.setView(loadView, 0, 0, 0, 0)
        mAlertDialog!!.setCancelable(false)
    }

    fun showProgressDialog() {

        mAlertDialog!!.show()


    }


    fun dismiss() {
        if (mAlertDialog != null && mAlertDialog!!.isShowing) {
            mAlertDialog!!.dismiss()
        }
    }
}



