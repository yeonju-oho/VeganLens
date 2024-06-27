package com.example.veganlens

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.veganlens.databinding.ActivityNicknameBinding
import com.example.veganlens.network.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NicknameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNicknameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNicknameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences: SharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)

        binding.btnSave.setOnClickListener {
            val nickname = binding.nicknameEditText.text.toString()

            // 닉네임 중복 확인
            NetworkService.service.checkNickname(CheckNicknameRequest(nickname))
                .enqueue(object : Callback<CheckNicknameResponse> {
                    override fun onResponse(call: Call<CheckNicknameResponse>, response: Response<CheckNicknameResponse>) {
                        if (response.isSuccessful) {
                            val checkResponse = response.body()
                            if (checkResponse != null) {
                                if (checkResponse.exists) {
                                    Toast.makeText(this@NicknameActivity, checkResponse.message, Toast.LENGTH_SHORT).show()
                                } else {
                                    // 중복되지 않으면 서버와 Local에 저장
                                    saveNicknameOnServer(nickname)
                                    sharedPreferences.edit().putString("nickname", nickname).apply()
                                    // TODO: 다음 액티비티로 이동하는 코드 작성 필요
                                }
                            }
                        } else {
                            Toast.makeText(this@NicknameActivity, "서버 오류 발생", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<CheckNicknameResponse>, t: Throwable) {
                        Toast.makeText(this@NicknameActivity, "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
                        t.printStackTrace()
                    }
                })
        }
    }

    private fun saveNicknameOnServer(nickname: String) {
        NetworkService.service.addNickname(AddNicknameRequest(nickname))
            .enqueue(object : Callback<AddNicknameResponse> {
                override fun onResponse(call: Call<AddNicknameResponse>, response: Response<AddNicknameResponse>) {
                    if (response.isSuccessful) {
                        val addResponse = response.body()
                        if (addResponse != null && addResponse.success) {
                            Toast.makeText(this@NicknameActivity, addResponse.message, Toast.LENGTH_SHORT).show()
                            // 저장 성공 처리
                        } else {
                            Toast.makeText(this@NicknameActivity, "닉네임 저장 실패", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@NicknameActivity, "서버 오류 발생", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AddNicknameResponse>, t: Throwable) {
                    Toast.makeText(this@NicknameActivity, "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
                    t.printStackTrace()
                }
            })
    }
}