package com.ssu.veganlens.myPages

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.ssu.veganlens.R
import com.ssu.veganlens.network.DeleteUserRequest
import com.ssu.veganlens.network.DeleteUserResponse
import com.ssu.veganlens.network.ImageService
import com.ssu.veganlens.network.NetworkService
import com.ssu.veganlens.network.UpdateUserRequest
import com.ssu.veganlens.network.UpdateUserResponse
import com.ssu.veganlens.startPages.RegistrationActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyPageFragment : Fragment() {

    private lateinit var tvIntro: TextView
    private lateinit var tvNickname: TextView
    private lateinit var tvVeganLevel: TextView
    private lateinit var tvDeleteAccount: TextView
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

        // 전역 username 초기화
        username = sharedPreferences.getString("nickname", "도토리") ?: "도토리"
        val veganLevel = sharedPreferences.getString("veganLevel", "폴로")
        val bio = sharedPreferences.getString("bio", "자기소개를 작성해 주세요") ?: "자기소개를 작성해 주세요"
        val profilePicture = sharedPreferences.getString("profilePicture", null)

        // 자기소개 TextView 및 기타 UI 요소 초기화
        tvNickname = view.findViewById(R.id.tvNickname) // 닉네임을 반영할 TextView 초기화
        tvIntro = view.findViewById(R.id.tvIntro)
        tvVeganLevel = view.findViewById(R.id.tvVeganLevel)
        ivProfileImage = view.findViewById(R.id.ivProfileImage)
        tvDeleteAccount = view.findViewById(R.id.tvDeleteAccount)

        // 텍스트뷰에 저장된 데이터 반영
        tvNickname.text = username
        tvIntro.text = bio
        tvVeganLevel.text = veganLevel
        if(!profilePicture.isNullOrBlank())  //이미지 있는 경우만 갱신
            ImageService.loadImageIntoImageView(requireContext(), profilePicture, ivProfileImage)

        // 자기소개 TextView 클릭 시 다이얼로그를 띄워 사용자 입력을 받습니다.
        tvIntro.setOnClickListener {
            showEditIntroDialog()
        }

        // 비건 레벨 클릭 시 비건 타입을 수정하는 로직
        tvVeganLevel.setOnClickListener {
            showVeganTypeDialog()
        }

        // 회원 탈퇴 클릭 이벤트
        tvDeleteAccount.setOnClickListener {
            showDeleteAccountDialog() // 회원 탈퇴 다이얼로그 호출
        }

        // 프로필 사진을 클릭했을 때 이벤트
        ivProfileImage.setOnClickListener{
            pickImageLauncher.launch("image/*")
        }

        return view
    }

    // 회원 탈퇴 다이얼로그
    private fun showDeleteAccountDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("회원탈퇴")
            .setMessage("회원탈퇴하시겠습니까?")
            .setPositiveButton("예") { dialog, _ ->
                deleteUser(username) // 회원 탈퇴 API 호출
                dialog.dismiss()
            }
            .setNegativeButton("아니오") { dialog, _ ->
                dialog.dismiss() // 다이얼로그 닫기
            }
            .show()
    }

    private fun deleteUser(username: String) {
        val request = DeleteUserRequest(username)

        // 비동기 방식으로 요청 보내기
        NetworkService.service.deleteUser(request).enqueue(object : Callback<DeleteUserResponse> {
            override fun onResponse(
                call: Call<DeleteUserResponse>,
                response: Response<DeleteUserResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        println("Success: ${responseBody.message}")
                        performLogout()
                    } else {
                        println("Failed: Null response body")
                    }
                } else {
                    println("Failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DeleteUserResponse>, t: Throwable) {
                println("Error: ${t.message}")
            }
        })
    }


    // 로그아웃 처리 및 초기 화면으로 이동
    private fun performLogout() {
        // SharedPreferences에서 사용자 데이터 삭제 (로그아웃 처리)
        try {
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // 로그인 화면으로 이동
        val intent = Intent(requireContext(), RegistrationActivity::class.java) // LoginActivity는 앱의 로그인 화면이어야 함
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // 이전 액티비티 스택을 모두 지우고 새 작업으로 시작
        startActivity(intent)

        // 현재 액티비티 종료
        requireActivity().finish()
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

                // 서버에 바이오 업데이트 요청
                val request = UpdateUserRequest(
                    profilePicture = null,
                    bio = newIntro,  //바이오 업데이트
                    reason = null,
                    veganType = null,
                )
                updateUserProfile(request)
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
                val request = UpdateUserRequest(
                    profilePicture = null,
                    bio = null,
                    reason = null,
                    veganType = veganTypeCode       //비건타입 업데이트
                )
                updateUserProfile(request)
            }
            .show()
    }

    // 사용자 정보 업데이트 API 호출
    private fun updateUserProfile(request: UpdateUserRequest) {

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

    // 이미지 선택해서 서버에 올리는 런쳐
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            //1. 서버에 이미지 업로드
            ImageService.uploadImage(requireContext(), it) { success, imageUrl ->
                if (success) {
                    //2. 기존에 서버에 올라가있던 이미지 삭제
                    var origImageUrl = sharedPreferences.getString("profilePicture", null)
                    if(!origImageUrl.isNullOrBlank())
                        ImageService.deleteImage(origImageUrl)

                    //3. DB에 프로필 Uri 업데이트 요청
                    val request = UpdateUserRequest(
                        profilePicture = imageUrl,  //프로필 사진 업데이트
                        bio = null,
                        reason = null,
                        veganType = null,
                    )
                    updateUserProfile(request)

                    //4. 이미지 경로를 SharedPreferences에 저장
                    sharedPreferences.edit().putString("profilePicture", imageUrl).apply()

                    //5. 화면 다시 갱신해주기
                    if (!imageUrl.isNullOrBlank()) {
                        ImageService.loadImageIntoImageView(requireContext(), imageUrl, ivProfileImage)
                    }
                } else {
                    // 업로드 실패 처리
                }
            }
        }
    }

}
