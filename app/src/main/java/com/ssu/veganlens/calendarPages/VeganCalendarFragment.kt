package com.ssu.veganlens.calendarPages

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.ssu.veganlens.R
import com.ssu.veganlens.databinding.FragmentVeganCalendarBinding
import com.ssu.veganlens.network.DiarySearchResponse
import com.ssu.veganlens.network.GetUserResponse
import com.ssu.veganlens.network.NetworkService
import com.ssu.veganlens.veganLogPages.VeganDiaryFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class VeganCalendarFragment : Fragment() {

    private lateinit var binding: FragmentVeganCalendarBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVeganCalendarBinding.inflate(inflater, container, false)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val username = sharedPreferences.getString("nickname", "도토리")
        getUserData(username!!) { duringTime ->
            binding.tvDuringTime.text = "비건렌즈와 함께한 지 ${duringTime + 1}일 째"
        }

        // 오늘 날짜의 데이터 가져옴
        getDiaryForDate(username, Calendar.getInstance().time)

        // 캘린더 날짜 선택 리스너
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // 선택한 날짜를 포맷
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.time

            // 서버에 요청
            getDiaryForDate(username, selectedDate)
        }

        // 플로팅 버튼 (ImageView) 클릭 이벤트
        binding.ivAddDiary.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, VeganDiaryAddFragment())
            transaction.addToBackStack(null) // 뒤로 가기 버튼으로 돌아갈 수 있게 함
            transaction.commit()
        }

        return binding.root
    }

    private fun getUserData(username: String, callback: (Long) -> Unit) {
        // API 요청
        NetworkService.service.getUser(username).enqueue(object : Callback<GetUserResponse> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<GetUserResponse>, response: Response<GetUserResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val createAt = response.body()!!.user?.createdAt

                    // 생성 날짜가 null인 경우 0 반환
                    if (createAt == null) {
                        callback(0)
                        return
                    }

                    // 날짜 형식에 맞는 DateTimeFormatter 생성
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                    try {
                        // 생성 날짜를 LocalDate로 변환
                        val creationDate = LocalDate.parse(createAt, formatter)

                        // 오늘 날짜 가져오기
                        val today = LocalDate.now()

                        // 생성 날짜와 오늘 날짜 간의 차이 계산
                        val diff = ChronoUnit.DAYS.between(creationDate, today)

                        // 계산된 차이를 콜백으로 반환
                        callback(diff)
                    } catch (e: Exception) {
                        // 예외 발생 시 0 반환
                        callback(0)
                    }
                } else {
                    // 서버 오류 또는 사용자를 찾을 수 없는 경우 0 반환
                    callback(0)
                    Toast.makeText(requireContext(), "서버에서 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                // 네트워크 오류 발생 시 0 반환
                callback(0)
                Toast.makeText(requireContext(), "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getDiaryForDate(username: String, selectedDate: Date) {

        // 날짜를 "yyyy-MM-dd" 형식으로 변환
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.format(selectedDate)

        //모든 아이템 제거
        binding.contextLayout.removeAllViews();

        // API 요청
        NetworkService.service.searchDiary(username, date).enqueue(object : Callback<DiarySearchResponse> {
            override fun onResponse(call: Call<DiarySearchResponse>, response: Response<DiarySearchResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    if (response.body()!!.diaries.isNotEmpty()) {
                        // 텍스트 변경
                        binding.tvToday.text = "${date}\t 작성한 게시글 목록"
                        binding.tvToday.visibility = View.VISIBLE

                        // 일기가 존재하면 하단에 등록해줌
                        for (diary in response.body()!!.diaries) {
                            val postView = LayoutInflater.from(requireContext()).inflate(R.layout.diary_post_v, binding.contextLayout, false)
                            val imageView = postView.findViewById<ImageView>(R.id.main_image)
                            val titleTextView = postView.findViewById<TextView>(R.id.title_text)

                            // 이미지를 설정
                            if (diary.images.isNotEmpty()) {
                                //TODO: 여기서 실제 이미지 설정하도록 해야 함
                            }
                            titleTextView.text = diary.title

                            // 아이템 클릭 리스너 추가 (클릭시 페이지 이동)
                            postView.setOnClickListener {
                                // 페이지 이동
                                val fragment = VeganDiaryFragment().newInstance(diary)
                                val transaction = parentFragmentManager.beginTransaction() // 또는 requireActivity().supportFragmentManager
                                transaction.replace(R.id.fragment_container, fragment) // fragment_container는 Fragment가 표시될 뷰의 ID입니다.
                                //transaction.addToBackStack(null) // 뒤로 가기 스택에 추가
                                transaction.commit()
                            }
                            // 인플레이트한 뷰를 contextLayout에 추가합니다.
                            binding.contextLayout.addView(postView)
                        }
                    } else {
                        // 일기가 없으면 아무 일도 하지 않음
                        binding.tvToday.visibility = View.GONE
                        // Toast.makeText(requireContext(), "해당 날짜의 일기가 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // 서버 오류 또는 사용자를 찾을 수 없는 경우
                    Toast.makeText(requireContext(), "서버에서 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DiarySearchResponse>, t: Throwable) {
                // 네트워크 오류 처리
                Toast.makeText(requireContext(), "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
