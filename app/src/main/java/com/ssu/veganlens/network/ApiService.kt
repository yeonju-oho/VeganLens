package com.ssu.veganlens.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Date

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

    @PATCH("/api/update-user/{username}")
    fun updateUser(
        @Path("username") username: String,
        @Body request: UpdateUserRequest
    ): Call<UpdateUserResponse>

    @HTTP(method = "DELETE", path = "/api/delete-user", hasBody = true)
    fun deleteUser(
        @Body request: DeleteUserRequest
    ): Call<DeleteUserResponse>

    @GET("/api/get-user/{username}")
    fun getUser(
        @Path("username") username: String
    ): Call<GetUserResponse>

    @POST("/api/add-diary")
    fun addDiray(@Body request: AddDiaryRequest): Call<AddDiaryResponse>

    @GET("/api/user-diaries/{username}")
    fun searchDiary(
        @Path("username") username: String,
        @Query("date") date: String? // Use @Query for query parameters
    ): Call<DiarySearchResponse>

    @GET("/api/public-diaries")
    fun getAllDiaries(
        //req 없음
    ): Call<DiarySearchResponse>

    @DELETE("/api/delete-diary/{id}")
    fun deleteDiary(
        @Path("id") id: String,
    ): Call<DeleteDiaryResponse>

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

//마이페이지 수정
data class UpdateUserRequest(
    val profilePicture: String?,
    val bio: String?,
    val reason: Int?,
    val veganType: Int?
)

data class UpdateUserResponse(
    val success: Boolean,
    val message: String,
    val user: UserData? // 업데이트된 사용자 정보
)

data class UserData(
    val username: String,
    val profilePicture: String,
    val bio: String,
    val reason: Int,
    val veganType: Int,
    val createdAt: String
)

// 회원탈퇴
data class DeleteUserResponse(
    val success: String,
    val message: String
)

data class DeleteUserRequest(
    val username: String
)

data class GetUserResponse(
    val success: Boolean,
    val user: UserData? // 업데이트된 사용자 정보
)

data class AddDiaryRequest(
    val username: String,
    val title: String,
    val content: String,
    val images: List<String>,
    val isPublic: Boolean
)

data class AddDiaryResponse(
    val success: Boolean,
    val message: String,
    val diary: Diary
)

data class Diary(
    val _id: String,
    val username: String,
    val userId : String,
    val title: String,
    val content: String,
    val images: List<String>,
    val publishedAt: Date,
    val likes: List<String>,
    val isPublic: Boolean
)

data class DiarySearchResponse(
    val success: Boolean,
    val diaries: List<Diary>
)

data class DeleteDiaryResponse(
    val success: Boolean,
    val message: String
)
