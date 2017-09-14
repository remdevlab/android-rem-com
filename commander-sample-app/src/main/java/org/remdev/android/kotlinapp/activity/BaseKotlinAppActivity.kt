package org.remdev.android.kotlinapp.activity

import android.app.ProgressDialog
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity

open class BaseKotlinAppActivity : AppCompatActivity() {
    private var progressDialog: ProgressDialog? = null
    private var destroyed = false

    override fun onDestroy() {
        super.onDestroy()
        destroyed = true
    }

    fun showProgress(message : String) {
        if (destroyed) {
            return
        }
        progressDialog?.let {
            it.setMessage(message)
            return
        }
        progressDialog = ProgressDialog.show(this, null, message, true, false);
    }

    fun showProgress(@StringRes msgId : Int) {
        showProgress(getString(msgId))
    }

    fun hideProgress() {
        if (destroyed) {
            return
        }
        progressDialog?.dismiss()
        progressDialog = null
    }
}
