package com.ssu.veganlens.veganLogPages

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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


class VeganDiaryFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var diaryTitleTextView: TextView
    private lateinit var diaryContentTextView: TextView
    private lateinit var userNameTextView: TextView
    private lateinit var publicTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var userProfileImageView: ImageView
    private lateinit var diaryImageView: ImageView
    private lateinit var editButton: Button
    private lateinit var deleteButton: Button

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
        val view = inflater.inflate(R.layout.fragment_vegan_diary, container, false)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val username = sharedPreferences.getString("nickname", "도토리")

        // 뷰 연결
        diaryTitleTextView = view.findViewById(R.id.diaryTitle)
        diaryContentTextView = view.findViewById(R.id.diaryContext)
        userNameTextView = view.findViewById(R.id.diaryUsername)
        publicTextView = view.findViewById(R.id.diaryPublic)
        dateTextView = view.findViewById(R.id.diaryDate)
        userProfileImageView = view.findViewById(R.id.diaryUserPic)
        diaryImageView = view.findViewById(R.id.diaryImageView)
        editButton = view.findViewById(R.id.editButton)
        deleteButton = view.findViewById(R.id.deleteButton)

        var id: String? = null // id 변수의 타입을 명시하고 초기화

        // Arguments에서 데이터 가져오기
        arguments?.let {
            beforePage = it.getInt("beforePage")

            id = it.getString("_id");
            diaryTitleTextView.text = it.getString("title")
            diaryContentTextView.text = it.getString("content")
            userNameTextView.text = it.getString("username")
            publicTextView.text =  it.getString("isPublic")
            dateTextView.text = it.getString("publishedAt")
            // 사용자 프로필 이미지 로드 (임시 이미지로 대체 가능)
            userProfileImageView.setImageResource(R.drawable.ic_profile) // 사용할 실제 리소스로 변경
            // Diary의 첫 번째 이미지 표시
            val images = it.getStringArrayList("images")
            if (!images.isNullOrEmpty()) {
                diaryImageView.setImageResource(R.drawable.vlog_image) // 사용할 실제 이미지 URL로 변경
            }
        }

        // 글이 글쓴이의 것이면
        if (userNameTextView.text.equals(username)) {
            editButton.visibility = View.GONE  //수정버튼은 일단 숨겨둠
            deleteButton.visibility = View.VISIBLE
        }
        else {
            editButton.visibility = View.GONE
            deleteButton.visibility = View.GONE
        }

        deleteButton.setOnClickListener{
            id?.let { it1 -> deleteDiaryById(it1) }
        }

        return view
    }

    // 예시 함수: 일기 삭제 요청을 보내고 응답을 처리하는 코드
    private fun deleteDiaryById(diaryId: String) {
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

}
