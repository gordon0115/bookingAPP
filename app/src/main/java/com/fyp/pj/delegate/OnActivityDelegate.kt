package com.fyp.pj.delegate

import android.content.Intent

interface OnActivityDelegate {
    fun showProgress(display: Boolean)
    fun showMessage(type: Int = 0, title: String? = null, message: String?, params: Intent? = null)
    fun go(intent: Intent, killBefore: Boolean = false)
    fun goForResult(intent: Intent, requestCode: Int = -1)
    fun goSingleTop(intent: Intent)
}