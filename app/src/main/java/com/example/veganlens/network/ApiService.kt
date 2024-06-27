package com.example.veganlens.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/api/checkNickname")
    fun checkNickname(@Body request: CheckNicknameRequest): Call<CheckNicknameResponse>

    @POST("/api/addNickname")
    fun addNickname(@Body request: AddNicknameRequest): Call<AddNicknameResponse>
}

data class CheckNicknameRequest(val nickname: String)
data class CheckNicknameResponse(val exists: Boolean, val message: String)
data class AddNicknameRequest(val nickname: String)
data class AddNicknameResponse(val success: Boolean, val message: String)