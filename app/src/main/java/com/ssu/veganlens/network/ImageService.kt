package com.ssu.veganlens.network

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.ssu.veganlens.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

object ImageService {

    // 서버에 이미지를 업로드 하고, 결과물 이미지 경로를 받는다.
    fun uploadImage(context: Context, uri: Uri, callback: (Boolean, String?) -> Unit) {
        val filePath = getRealPathFromURI(context, uri)
        val file = File(filePath)

        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("image", file.name, requestBody)

        NetworkService.service.uploadImage(part).enqueue(object : Callback<ImageResponse> {
            override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
                if (response.isSuccessful) {
                    val imageUrl = response.body()?.imageUrl
                    callback(true, imageUrl)
                } else {
                    Log.e("ImageUploader", "Upload failed: ${response.code()}, ${response.errorBody()?.string()}")
                    callback(false, null)
                }
            }

            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {

                Log.e("ImageUploader", "onFailure: ${t.message}")
                callback(false, null)
            }
        })
    }

    private fun getRealPathFromURI(context: Context, uri: Uri): String {
        var path = ""
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.let {
            if (it.moveToFirst()) {
                val idx = it.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                path = it.getString(idx)
            }
            it.close()
        }
        return path
    }

    // 서버에서 이미지를 삭제 요청하는 함수
    fun deleteImage(imageUrl: String) {
        NetworkService.service.deleteImage(imageUrl).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    // 삭제 성공
                    val successMessage = response.body()?.message
                    println("Image deleted: $successMessage")
                    // 필요 시 UI 업데이트 (이미지 삭제 후, 이미지뷰에서 제거 등)
                } else {
                    // 실패 처리
                    println("Failed to delete image: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                // 네트워크 오류 처리
                println("Network error: ${t.message}")
            }
        })
    }

    // 이미지 URL을 받아서 ImageView에 로드하는 함수
    fun loadImageIntoImageView(context: Context, imageUrl: String, imageView: ImageView) {
        Glide.with(context)
            .load(imageUrl)  // 이미지 URL
            .error(R.drawable.logo)  // 오류 시 보여줄 기본 이미지
            .into(imageView)  // ImageView에 로드
    }
    fun loadImageIntoImageView(context: Context, uri: Uri, imageView: ImageView) {
        Glide.with(context)
            .load(uri)  // Uri로 이미지를 로드
            .error(R.drawable.logo)  // 오류 시 보여줄 기본 이미지
            .into(imageView)  // ImageView에 로드
    }

}
