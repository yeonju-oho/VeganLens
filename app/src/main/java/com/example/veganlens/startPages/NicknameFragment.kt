package com.example.veganlens.startPages

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.veganlens.R
import com.example.veganlens.databinding.FragmentNicknameBinding
import com.example.veganlens.network.AddNicknameRequest
import com.example.veganlens.network.AddNicknameResponse
import com.example.veganlens.network.CheckNicknameRequest
import com.example.veganlens.network.CheckNicknameResponse
import com.example.veganlens.network.NetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NicknameFragment : Fragment() {

    private lateinit var binding: FragmentNicknameBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNicknameBinding.inflate(inflater, container, false)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        binding.buttonNext.setOnClickListener {
            val nickname = binding.editTextNickname.text.toString()
            if (nickname.isNotEmpty()) {
                sharedPreferences.edit().putString("nickname", nickname).apply()
                findNavController().navigate(R.id.action_NicknameFragment_to_VeganReasonFragment)
            } else {
                binding.editTextNickname.error = "닉네임을 입력해 주세요."
            }
        }
        
        /* DB API 호출 코드 예시
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
        }*/

        return binding.root
    }

    private fun saveNicknameOnServer(nickname: String) {
        NetworkService.service.addNickname(AddNicknameRequest(nickname))
            .enqueue(object : Callback<AddNicknameResponse> {
                override fun onResponse(call: Call<AddNicknameResponse>, response: Response<AddNicknameResponse>) {
                    if (response.isSuccessful) {
                        val addResponse = response.body()
                        if (addResponse != null && addResponse.success) {
                            // 저장 성공 처리
                        } else {
                            // 닉네임 저장 실패 처리
                        }
                    } else {
                        // 서버 오류 발생
                    }
                }

                override fun onFailure(call: Call<AddNicknameResponse>, t: Throwable) {
                    // 네트워크 오류 발생
                    t.printStackTrace()
                }
            })
    }
}
