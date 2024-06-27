package com.example.veganlens.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkService {

    //TODO : 서버 IP 주소 LOCAL이 아니도록 변경 필요
    private const val BASE_URL = "http://192.168.0.3:3000/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: ApiService = retrofit.create(ApiService::class.java)
}