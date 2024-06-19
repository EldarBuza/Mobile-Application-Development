package com.example.prvaspiralaeldarbuzadzic19398

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiAdapter {
    val retrofit : TrefleApi = Retrofit.Builder()
        .baseUrl("http://trefle.io/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TrefleApi::class.java)
}