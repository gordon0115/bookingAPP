package com.fyp.pj.delegate

import android.content.Intent
import com.fyp.pj.view.base.FragmentImpl

interface OnFgActivityDelegate {
    fun sendFragmentIntent(tag: String, requestCode: Int, intent: Intent)
    fun sendFragmentPermissionsResult(tag: String, requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    fun <T : FragmentImpl> go(fragment: T, tag: String, isReplace: Boolean)
    fun <T : FragmentImpl> go(fragment: T, tag: String)
    fun <T : FragmentImpl> swap(position: Int, fragment: T, tag: String)
    fun popBackStack(): Boolean
}