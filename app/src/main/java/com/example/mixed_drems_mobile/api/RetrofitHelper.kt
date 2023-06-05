package com.example.mixed_drems_mobile.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private val baseUrl = "https://10.0.2.2:7239/api/";

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}