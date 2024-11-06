package com.ssu.veganlens.veganLogPages

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.ssu.veganlens.R
import com.ssu.veganlens.network.DeleteDiaryResponse
import com.ssu.veganlens.network.Diary
import com.ssu.veganlens.network.NetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.navigation.fragment.findNavController
import com.ssu.veganlens.calendarPages.VeganCalendarFragment
import com.ssu.veganlens.databinding.FragmentVeganDiaryAddBinding
import com.ssu.veganlens.databinding.FragmentVeganDiaryBinding
import com.ssu.veganlens.network.GetUserResponse
import com.ssu.veganlens.network.ImageService
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


class VeganDiaryFragment : Fragment() {

    private lateinit var binding: FragmentVeganDiaryBinding
    private lateinit var sharedPreferences: SharedPreferences

    private var beforePage = 0  //이전 Page, 캘린더->1, Log->2

    // Diary 객체를 받아오는 함수
    fun newInstance(diary: Diary, beforePage: Int): VeganDiaryFragment {
        val fragment = VeganDiaryFragment()
        val args = Bundle()
        args.putString("_id", diary._id)
        args.putString("title", diary.title)
        args.putString("content", diary.content)
        args.putString("username", diary.username)
        args.putString("isPublic", if (diary.isPublic) "(공개)" else "(비공개)")
        args.putString("publishedAt", SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(diary.publishedAt))
        args.putStringArrayList("images", ArrayList(diary.images))
        args.putInt("beforePage", beforePage)
        fragment.arguments = args
        return fragment
    }

    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVeganDiaryBinding.inflate(inflater, container, false)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val username = sharedPreferences.getString("nickname", "도토리")
        var id: String? = null // id 변수의 타입을 명시하고 초기화

        // Arguments에서 데이터 가져오기
        arguments?.let {
            beforePage = it.getInt("beforePage")

            id = it.getString("_id")
            binding.diaryTitle.text = it.getString("title")
            binding.diaryContext.text = it.getString("content")
            binding.diaryUsername.text = it.getString("username")
            binding.diaryPublic.text =  it.getString("isPublic")
            binding.diaryDate.text = it.getString("publishedAt")

            // 사용자 프로필 이미지 로드
            val diaryUsername = binding.diaryUsername.text.toString()
            getUserPicString(diaryUsername) { userPic ->
                if (userPic.isNotBlank()) {
                    ImageService.loadImageIntoImageView(requireContext(), userPic, binding.diaryUserPic)
                }
            }

            // Diary의 이미지 로드
            val images = it.getStringArrayList("images")
            if (!images.isNullOrEmpty()) {
                displayImages(images)
            } else { // 이미지 없으면 이미지 영역 표시하지 않음
                binding.diaryImageLayout.visibility = View.GONE
            }

            // 글이 글쓴이의 것이면
            if (binding.diaryUsername.text.equals(username)) {
                binding.editButton.visibility = View.GONE // 수정버튼은 일단 숨겨둠
                binding.deleteButton.visibility = View.VISIBLE
            } else {
                binding.editButton.visibility = View.GONE
                binding.deleteButton.visibility = View.GONE
            }

            binding.deleteButton.setOnClickListener {
                id?.let { diaryId ->
                    deleteDiaryById(diaryId, images) // 삭제 시 id와 images 리스트를 전달
                }
            }
        }

        return binding.root
    }

    // 일기 삭제 요청을 보내고 응답을 처리하는 코드
    private fun deleteDiaryById(diaryId: String, images: ArrayList<String>?) {
        // 서버에 올라가있는 이미지부터 삭제한다.
        images?.forEachIndexed { index, url ->
            if (url.isNotEmpty()) {
                ImageService.deleteImage(url)
            }
        }

        // DB에 있는 게시물 정보를 삭제한다.
        NetworkService.service.deleteDiary(diaryId).enqueue(object : Callback<DeleteDiaryResponse> {
            override fun onResponse(
                call: Call<DeleteDiaryResponse>,
                response: Response<DeleteDiaryResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.success == true) {
                        println("일기 삭제 성공: ${responseBody.message}")
                        // 삭제 성공시 페이지 이동
                        if (beforePage == 1) {    //캘린더
                            val transaction = parentFragmentManager.beginTransaction() // 또는 requireActivity().supportFragmentManager
                            transaction.replace(R.id.fragment_container, VeganCalendarFragment()) // fragment_container는 Fragment가 표시될 뷰의 ID입니다.
                            transaction.commit()
                        }
                        else if(beforePage == 2) {  //Log
                            val transaction = parentFragmentManager.beginTransaction() // 또는 requireActivity().supportFragmentManager
                            transaction.replace(R.id.fragment_container, VeganLogFragment()) // fragment_container는 Fragment가 표시될 뷰의 ID입니다.
                            transaction.commit()
                        }
                    } else {
                        println("일기 삭제 실패: ${responseBody?.message ?: "알 수 없는 오류"}")
                    }
                } else {
                    println("서버 응답 오류: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DeleteDiaryResponse>, t: Throwable) {
                println("네트워크 오류: ${t.message}")
            }
        })
    }


    // 프로필 사진을 가져오기 위해 유저 정보를 가져오는 함수
    private fun getUserPicString(username: String, callback: (String) -> Unit) {
        // API 요청
        NetworkService.service.getUser(username).enqueue(object : Callback<GetUserResponse> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<GetUserResponse>, response: Response<GetUserResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()!!.user?.let { callback(it.profilePicture) }
                } else {
                    // 서버 오류 또는 사용자를 찾을 수 없는 경우 빈 문자열 반환
                    callback("")
                    Toast.makeText(requireContext(), "서버에서 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                // 네트워크 오류 발생 시 빈 문자열 반환
                callback("")
                Toast.makeText(requireContext(), "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 이미지 디스플레이 하는 함수
    fun displayImages(imageUrls: List<String>) {
        // 이미지 URL이 ""인 경우는 로드하지 않음
        val validUrls = imageUrls.filter { it.isNotBlank() }

        // 각 이미지뷰 가져오기
        val imageView1 = binding.diaryImageView1
        val imageView2 = binding.diaryImageView2
        val imageView3 = binding.diaryImageView3

        // 모든 이미지뷰 초기화
        imageView1.visibility = View.GONE
        imageView2.visibility = View.GONE
        imageView3.visibility = View.GONE

        // 이미지 뷰 목록 (순서대로 imageView1, imageView2, imageView3)
        val imageViews = listOf(imageView1, imageView2, imageView3)

        // 이미지 뷰의 visibility 설정 및 URL 로드
        validUrls.forEachIndexed { index, url ->
            if (index < imageViews.size) {
                imageViews[index].visibility = View.VISIBLE
                ImageService.loadImageIntoImageView(requireContext(), url, imageViews[index])
            }
        }
    }

}
