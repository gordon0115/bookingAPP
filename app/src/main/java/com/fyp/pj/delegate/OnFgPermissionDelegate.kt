package com.fyp.pj.delegate

interface OnFgPermissionDelegate {
    fun onFgPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
}