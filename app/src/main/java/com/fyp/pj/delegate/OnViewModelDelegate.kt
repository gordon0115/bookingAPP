package com.fyp.pj.delegate


interface OnViewModelDelegate {
    fun whenError(throwable: Throwable)
    fun whenLoading(showing: Boolean)
}