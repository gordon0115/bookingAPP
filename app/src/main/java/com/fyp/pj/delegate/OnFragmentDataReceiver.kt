package com.fyp.pj.delegate

import android.content.Intent

interface OnFragmentDataReceiver {
    fun onFragmentUpdate(requestCode: Int, intent: Intent)
}