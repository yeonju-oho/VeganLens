package com.example.veganlens.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("/api/add-ingredient")
    fun addIngredient(
        @Body request: AddIngredientRequest
    ): Call<AddIngredientResponse>

    @GET("/api/check-ingredients")
    fun checkIngredients(
        @Query("items") items: String
    ): Call<IngredientsResponse>

    @GET("/api/ingredients/{category}")
    fun getIngredientsByCategory(
        @Path("category") category: String
    ): Call<AllIngredientsResponse>

    @DELETE("/api/ingredients/{category}/{name}")
    fun deleteIngredient(
        @Path("category") category: String,
        @Path("name") name: String
    ): Call<DeleteIngredientResponse>

    @POST("/api/check-user")
    fun checkUsername(@Body request: UsernameRequest): Call<UsernameResponse>

    @POST("/api/add-user")
    fun addUser(@Body request: AddUserRequest): Call<AddUserResponse>
}

data class AddIngredientRequest(
    val category: String,
    val name : String
)

data class AddIngredientResponse(
    val message: String?,
    val error: String?
)

data class IngredientsResponse(
    val ingredients: Map<String, String>,
    val suitableVeganTypes: List<Int>
)

data class AllIngredientsResponse(
    val category: String,
    val ingredients: List<String>,
    val error: String? = null
)

data class DeleteIngredientResponse(
    val message: String?,
    val deletedIngredient: String?,
    val error: String? = null
)

// 요청에 사용할 데이터 클래스
data class UsernameRequest(val username: String)

// 응답에 사용할 데이터 클래스
data class UsernameResponse(
    val exists: Boolean,
    val message: String
)

//회원가입시, 닉네임 저장
data class AddUserRequest(
    val username: String,
    val isAdmin: Boolean,
    val profilePicture: String,
    val bio: String,
    val reason: Int,
    val veganType: Int
)

data class AddUserResponse(
    val success: Boolean,
    val message: String
)