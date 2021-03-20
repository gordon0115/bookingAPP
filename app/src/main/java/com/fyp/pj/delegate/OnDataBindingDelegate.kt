package com.fyp.pj.delegate

import androidx.databinding.ViewDataBinding


interface OnDataBindingDelegate<T : ViewDataBinding> {
    fun onCreateDataBinding(dataBinding: T): T?
}