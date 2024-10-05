package com.example.veganlens.startPages

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.veganlens.R
import com.example.veganlens.databinding.FragmentNicknameBinding
import com.example.veganlens.network.ApiService
import com.example.veganlens.network.NetworkService
import com.example.veganlens.network.UsernameRequest
import com.example.veganlens.network.UsernameResponse
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
            // 입력된 닉네임에서 앞뒤 공백을 제거합니다.
            val nickname = binding.editTextNickname.text.toString().trim()

            if (nickname.isNotEmpty()) {
                // 공백만 있는 경우를 방지하기 위해 공백 제거 후 체크
                checkUsernameAvailability(nickname)  // 닉네임 중복 확인
            } else {
                binding.editTextNickname.error = "닉네임을 입력해 주세요."
            }
        }

        return binding.root
    }

    private fun checkUsernameAvailability(username: String) {
        val api = NetworkService.service
        val request = UsernameRequest(username)

        api.checkUsername(request).enqueue(object : Callback<UsernameResponse> {
            override fun onResponse(
                call: Call<UsernameResponse>,
                response: Response<UsernameResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        if (result.exists) {
                            // 닉네임 중복 시
                            Toast.makeText(requireContext(), "${username}은(는) 이미 사용 중인 닉네임입니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            // 닉네임 사용 가능 시 SharedPreferences에 저장
                            saveNicknameToSharedPreferences(username)
                            findNavController().navigate(R.id.action_NicknameFragment_to_VeganReasonFragment)
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "요청 실패: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UsernameResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "서버 오류 발생", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 닉네임을 SharedPreferences에 저장하는 함수
    private fun saveNicknameToSharedPreferences(nickname: String) {
        val editor = sharedPreferences.edit()
        editor.putString("nickname", nickname)  // 닉네임 저장
        editor.apply()  // 변경 사항을 비동기적으로 저장
        //Toast.makeText(requireContext(), "닉네임이 저장되었습니다.", Toast.LENGTH_SHORT).show()
    }
}



//    //구 format 형식
//    private fun checkDB(nickname: String) {
//        NetworkService.service.checkNickname(CheckNicknameRequest(nickname))
//        .enqueue(object : Callback<CheckNicknameResponse> {
//            override fun onResponse(call: Call<CheckNicknameResponse>, response: Response<CheckNicknameResponse>) {
//                if (response.isSuccessful) {
//                    val checkResponse = response.body()
//                    if (checkResponse != null) {
//                        if (checkResponse.exists) {
//                            //TODO: Toast 사용 xxx
//                            Toast.makeText(requireContext(), checkResponse.message, Toast.LENGTH_SHORT).show()
//                        } else {
//                            // 중복되지 않으면 서버와 Local에 저장
//                            saveNicknameOnServer(nickname)
//                            sharedPreferences.edit().putString("nickname", nickname).apply()
//
//                            //페이지 이동
//                            findNavController().navigate(R.id.action_NicknameFragment_to_VeganReasonFragment)
//                        }
//                    }
//                } else {
//                    //TODO: Toast 사용 xxx
//                    Toast.makeText(requireContext(), "서버 오류 발생", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<CheckNicknameResponse>, t: Throwable) {
//                Toast.makeText(requireContext(), "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
//                t.printStackTrace()
//            }
//        })
//    }
//
//    private fun saveNicknameOnServer(nickname: String) {
//        NetworkService.service.addNickname(AddNicknameRequest(nickname))
//            .enqueue(object : Callback<AddNicknameResponse> {
//                override fun onResponse(call: Call<AddNicknameResponse>, response: Response<AddNicknameResponse>) {
//                    if (response.isSuccessful) {
//                        val addResponse = response.body()
//                        if (addResponse != null && addResponse.success) {
//                            // 저장 성공 처리
//                        } else {
//                            // 닉네임 저장 실패 처리
//                        }
//                    } else {
//                        // 서버 오류 발생
//                    }
//                }
//
//                override fun onFailure(call: Call<AddNicknameResponse>, t: Throwable) {
//                    // 네트워크 오류 발생
//                    t.printStackTrace()
//                }
//            })
//    }

