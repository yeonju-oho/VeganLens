package com.example.veganlens.myPages

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.veganlens.R
import com.example.veganlens.network.NetworkService
import com.example.veganlens.network.UpdateUserRequest
import com.example.veganlens.network.UpdateUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageFragment : Fragment() {

    private lateinit var tvIntro: TextView
    private lateinit var tvNickname: TextView
    private lateinit var tvVeganLevel: TextView
    private lateinit var ivProfileImage: ImageView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 프래그먼트 레이아웃을 inflate합니다.
        val view = inflater.inflate(R.layout.fragment_mypage, container, false)

        // SharedPreferences 초기화
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        // SharedPreferences에서 닉네임, 비건 단계, 자기소개 불러오기
        username = sharedPreferences.getString("nickname", "사용자닉네임") ?: "사용자닉네임"
        val veganLevel = sharedPreferences.getString("veganLevel", "폴로")
        val bio = sharedPreferences.getString("bio", "자기소개를 작성해 주세요") ?: "자기소개를 작성해 주세요"

        // 자기소개 TextView 및 기타 UI 요소 초기화
        tvNickname = view.findViewById(R.id.tvNickname) // 닉네임을 반영할 TextView 초기화
        tvIntro = view.findViewById(R.id.tvIntro)
        tvVeganLevel = view.findViewById(R.id.tvVeganLevel)
        ivProfileImage = view.findViewById(R.id.ivProfileImage)

        // 텍스트뷰에 저장된 데이터 반영
        tvNickname.text = username
        tvIntro.text = bio
        tvVeganLevel.text = veganLevel

        // 자기소개 TextView 클릭 시 다이얼로그를 띄워 사용자 입력을 받습니다.
        tvIntro.setOnClickListener {
            showEditIntroDialog()
        }

        // 비건 레벨 클릭 시 비건 타입을 수정하는 로직
        tvVeganLevel.setOnClickListener {
            showVeganTypeDialog()
        }

        return view
    }

    // 사용자 자기소개 수정 다이얼로그
    private fun showEditIntroDialog() {
        val editText = EditText(requireContext()).apply {
            hint = "자기소개를 입력하세요"
            setText(tvIntro.text) // 기존의 자기소개를 기본 값으로 설정합니다.
        }

        AlertDialog.Builder(requireContext())
            .setTitle("자기소개 수정")
            .setView(editText)
            .setPositiveButton("저장") { dialog, _ ->
                val newIntro = editText.text.toString().trim()
                tvIntro.text = newIntro // 사용자가 입력한 텍스트를 TextView에 반영합니다.

                // 자기소개를 SharedPreferences에 저장
                sharedPreferences.edit().putString("bio", newIntro).apply()

                // 서버에 업데이트 요청
                updateUserProfile(bio = newIntro)
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // 비건 타입 선택 다이얼로그
    private fun showVeganTypeDialog() {
        val veganTypes = arrayOf("비건", "락토 베지테리언", "오보 베지테리언", "락토 오보 베지테리언", "페스코 베지테리언", "폴로 베지테리언", "아직 채식을 시작하지 않았어!")

        AlertDialog.Builder(requireContext())
            .setTitle("비건 타입 선택")
            .setItems(veganTypes) { _, which ->
                val selectedType = veganTypes[which]
                tvVeganLevel.text = selectedType // 선택된 비건 레벨을 텍스트뷰에 반영
                val veganTypeCode = when (selectedType.trim()) {
                    "비건" -> 1
                    "락토 베지테리언" -> 2
                    "오보 베지테리언" -> 3
                    "락토 오보 베지테리언" -> 4
                    "페스코 베지테리언" -> 5
                    "폴로 베지테리언" -> 6
                    "아직 채식을 시작하지 않았어!" -> 7 // 플렉시테리언
                    else -> 7
                }

                // 비건 레벨을 SharedPreferences에 저장
                sharedPreferences.edit().putString("veganLevel", selectedType).apply()

                // 비건 타입 업데이트 요청
                updateUserProfile(veganType = veganTypeCode)
            }
            .show()
    }

    // 사용자 정보 업데이트 API 호출
    private fun updateUserProfile(bio: String? = null, veganType: Int? = null) {
        // 업데이트할 사용자 정보 요청 생성
        val request = UpdateUserRequest(
            profilePicture = null, // 프로필 사진 업데이트는 생략
            bio = bio,
            reason = null, // 비건 이유 업데이트는 생략
            veganType = veganType
        )

        // 서버에 업데이트 요청
        NetworkService.service.updateUser(username, request).enqueue(object : Callback<UpdateUserResponse> {
            override fun onResponse(call: Call<UpdateUserResponse>, response: Response<UpdateUserResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(requireContext(), "업데이트 성공", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "업데이트 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateUserResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "서버 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
