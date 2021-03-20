package com.fyp.pj.retrofit

import com.fyp.pj.model.ServicesSetterGetter
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("todos")
    fun getServices() : Call<List<ServicesSetterGetter>>

}