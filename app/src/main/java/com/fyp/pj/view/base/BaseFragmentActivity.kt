package com.fyp.pj.view.base

import android.content.Intent
import android.os.Bundle
import com.fyp.pj.delegate.OnFgActivityDelegate
import com.fyp.pj.delegate.OnFragmentDataReceiver
import com.fyp.pj.delegate.OnFgPermissionDelegate
import com.fyp.pj.tools.DialogHelper
import com.fyp.pj.tools.FragmentHelper
import com.fyp.pj.view.activity.ActivityImpl

abstract class BaseFragmentActivity: ActivityImpl(), OnFgActivityDelegate {

    private lateinit var fragmentHelper: FragmentHelper
    private var dialogHelper: DialogHelper? = null

    abstract fun getFragmentLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentHelper = FragmentHelper(supportFragmentManager)

        savedInstanceState?.getParcelable<FragmentHelper.FragmentInfo>("fgnfo")?.also {
            fragmentHelper.setFragmentInfo(it)
        }

        fragmentHelper.setFrameId(getFragmentLayoutId())
        dialogHelper = DialogHelper(this)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable("fgnfo", getFragmentHelper().getFragmentInfo())
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        var curPosition = fragmentHelper.getCurrentPosition()

        if (!fragmentHelper.isBackBlock()) {
            super.onBackPressed()
        }else{
            if (curPosition != fragmentHelper.getCurrentPosition()) { }
        }
    }

    fun getFragmentHelper(): FragmentHelper {
        return fragmentHelper
    }

    private fun getFragmentDataReceiver(tag: String): OnFragmentDataReceiver? {
        val fragment = fragmentHelper.findByTag(tag)
        if(fragment is OnFragmentDataReceiver){
            return fragment
        }

        return null
    }

    private fun getFragmentPermissionReceiver(tag: String): OnFgPermissionDelegate? {
        val fragment = fragmentHelper.findByTag(tag)
        if(fragment is OnFgPermissionDelegate){
            return fragment
        }

        return null
    }

    override fun sendFragmentIntent(tag: String, requestCode: Int, intent: Intent) {
        getFragmentDataReceiver(tag)?.onFragmentUpdate(requestCode, intent)
    }

    override fun sendFragmentPermissionsResult(tag: String, requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        getFragmentPermissionReceiver(tag)?.onFgPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun <T : FragmentImpl> go(fragment: T, tag: String, isReplace: Boolean) {
        if (isReplace) {
            fragmentHelper.replace(fragment, tag)
        } else {
            fragmentHelper.add(fragment, tag)
        }
    }

    override fun <T : FragmentImpl> go(fragment: T, tag: String) {
        fragmentHelper.add(fragment, tag)
    }

    override fun <T : FragmentImpl> swap(position: Int, fragment: T, tag: String) {
        fragmentHelper.swap(position, fragment, tag)
    }

    override fun popBackStack(): Boolean {
        return fragmentHelper.pop()
    }

    override fun onPause() {
        super.onPause()
        dialogHelper?.dismiss()
    }
}