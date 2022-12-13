package com.mohamed_amgd.fpl_assistant.data.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {
    private val baseUrl = "http://fpl-stats-server-production.up.railway.app/"

    fun getInstance(): ApiInterface {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}