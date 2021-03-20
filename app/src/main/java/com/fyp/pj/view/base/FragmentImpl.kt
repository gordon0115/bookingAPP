package com.fyp.pj.view.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.fyp.pj.delegate.OnActivityDelegate
import com.fyp.pj.delegate.OnDataBindingDelegate
import com.fyp.pj.delegate.OnFgActivityDelegate
import com.fyp.pj.tools.FragmentHelper

abstract class FragmentImpl : Fragment(), FragmentHelper.OnFragmentInterface {

    var TAG = javaClass.simpleName
    private var onActivityDelegate: OnActivityDelegate? = null
    private var onFragmentActivityDelegate: OnFgActivityDelegate? = null

    protected abstract fun getLayoutId(): Int

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnActivityDelegate) {
            onActivityDelegate = context
        }

        if (context is OnFgActivityDelegate) {
            onFragmentActivityDelegate = context
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (savedInstanceState != null) {
            arguments = savedInstanceState.getBundle(tag)
        }

        if (getLayoutId() > 0) {
            if (this is OnDataBindingDelegate<*>) {
                val binding = onCreateDataBinding(DataBindingUtil.inflate(inflater, getLayoutId(), container, false))
                binding?.lifecycleOwner = this
                return binding?.root
            } else {
                return inflater.inflate(getLayoutId(), container, false)
            }
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }
    
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBundle(tag, arguments)
        super.onSaveInstanceState(outState)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            onPause()
        } else {
            onResume()
        }
    }

    fun getActivityDelegate(): OnActivityDelegate? {
        return onActivityDelegate
    }

    fun getFragmentActivityDelegate(): OnFgActivityDelegate? {
        return onFragmentActivityDelegate
    }

    protected fun popBackStack() {
        onFragmentActivityDelegate?.popBackStack()
    }

    protected fun <T1 : FragmentImpl> go(fragment: T1, tag: String, isReplace: Boolean) {
        onFragmentActivityDelegate?.go(fragment, tag, isReplace)
    }

    protected fun <T1 : FragmentImpl> go(fragment: T1, tag: String) {
        onFragmentActivityDelegate?.go(fragment, tag, false)
    }

    protected fun showProgress(display: Boolean) {
        onActivityDelegate?.showProgress(display)
    }

    override fun isBackBlock(): Boolean {
        return false
    }
}