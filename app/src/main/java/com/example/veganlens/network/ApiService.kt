package com.example.veganlens.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("/api/check-ingredients")
    fun checkIngredients(@Query("items") items: String): Call<IngredientsResponse>
}

data class IngredientsResponse(
    val ingredients: Map<String, String>,
    val suitableVeganTypes: List<Int>
)