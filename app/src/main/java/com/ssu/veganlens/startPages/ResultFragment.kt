package com.ssu.veganlens.startPages

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.ssu.veganlens.MainActivity
import com.ssu.veganlens.databinding.FragmentResultBinding
import android.util.Log
import android.widget.Toast
import com.ssu.veganlens.network.NetworkService
import com.ssu.veganlens.network.AddUserRequest
import com.ssu.veganlens.network.AddUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        // 닉네임과 비건 단계를 가져와서 바인딩합니다.
        val nickname = sharedPreferences.getString("nickname", "도토리")
        val veganLevel = sharedPreferences.getString("veganLevel", "폴로")
        val veganReason = sharedPreferences.getInt("veganReason", 1) // 기본값을 HEALTH_CONCERNS로 설정

        binding.textViewNickname.text = nickname
        binding.textViewVeganLevel.text = veganLevel

        binding.buttonFinish.setOnClickListener {
            completeSignup(nickname!!, veganLevel!!, veganReason)
        }

        return binding.root
    }

    private fun completeSignup(nickname: String, veganLevel: String, reason: Int) {
        // 회원가입 처리가 완료된 후 SharedPreferences에 데이터 저장
        val editor = sharedPreferences.edit()
        editor.putBoolean("isRegistered", true)  // 회원가입 완료 상태를 저장
        editor.apply()

        // 값 확인
        val isRegistered = sharedPreferences.getBoolean("isRegistered", false)
        Log.d("SharedPreferences", "isRegistered: $isRegistered")

        // DB에 저장할 데이터를 준비
        val request = AddUserRequest(
            username = nickname,
            isAdmin = false,  // 이 예시에서는 일반 사용자를 저장
            profilePicture = "https://default-pic-url.com",  // 기본 프로필 이미지 URL을 설정
            bio = "소개글",  // 유저의 간단한 소개
            reason = reason,  // 비건 이유 코드 저장
            veganType = when (veganLevel.trim()) {  // 공백을 제거한 후 비교
                "비건" -> 1
                "락토 베지테리언" -> 2
                "오보 베지테리언" -> 3
                "락토 오보 베지테리언" -> 4
                "페스코 베지테리언" -> 5
                "폴로 베지테리언" -> 6
                "플렉시테리언" -> 7
                "아직 채식을 시작하지 않았어!" -> 7 //플렉시테리언
                else -> 7 // 기본값을 플렉시테리언으로 설정
            }
        )

        // 서버에 데이터를 전송하는 부분
        NetworkService.service.addUser(request).enqueue(object : Callback<AddUserResponse> {
            override fun onResponse(
                call: Call<AddUserResponse>,
                response: Response<AddUserResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null && result.success) {
                        // 성공적인 응답 처리
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()

                        // 메인 화면으로 이동하는 코드 추가
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish() // 현재 액티비티를 종료하여 뒤로 가기 버튼 누를 때 다시 이 화면이 나오지 않도록 함
                    } else {
                        Toast.makeText(requireContext(), "사용자 추가에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "요청 실패: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AddUserResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "서버 오류 발생", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
