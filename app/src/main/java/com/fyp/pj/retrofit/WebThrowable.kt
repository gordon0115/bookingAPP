package com.fyp.pj.retrofit

import android.content.Context

class WebThrowable(var context: Context?,
                   var code: Int? = -1,
                   message: String? = null) : Throwable(message){

    var title: String? = null
    val titls: HashMap<Int, String> = hashMapOf()
    val error: HashMap<Int, String> = hashMapOf()

    fun getErrorMessage(): String? {
        return error[code] ?: message
    }


}