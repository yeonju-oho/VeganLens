import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.veganlens.DiaryDetailFragment
import com.example.veganlens.R
import com.example.veganlens.databinding.FragmentVeganCalendarBinding
import com.example.veganlens.network.DiarySearchResponse
import com.example.veganlens.network.NetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
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

        var duringTime = 200;
        binding.tvDuringTime.text = "비건렌즈와 함께한 지 ${duringTime}일 째"

        // 캘린더 날짜 선택 리스너
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // 선택한 날짜를 포맷
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.time

            // 날짜를 "yyyy-MM-dd" 형식으로 변환
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate)
            val username = sharedPreferences.getString("nickname", "도토리")

            // 서버에 요청
            if (username != null) {
                getDiaryForDate(username, formattedDate)
            }
        }

        // 플로팅 버튼 (ImageView) 클릭 이벤트
        binding.ivAddDiary.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, VeganDiaryDetailFragment())
            transaction.addToBackStack(null) // 뒤로 가기 버튼으로 돌아갈 수 있게 함
            transaction.commit()
        }

        return binding.root
    }

    private fun getDiaryForDate(username: String, date: String) {
        // API 요청
        NetworkService.service.searchDiary(username, date).enqueue(object : Callback<DiarySearchResponse> {
            override fun onResponse(call: Call<DiarySearchResponse>, response: Response<DiarySearchResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    if (response.body()!!.diaries.isNotEmpty()) {
                        // 일기가 존재하면 상세 화면으로 이동
                        val fragment = DiaryDetailFragment().newInstance(response.body()!!.diaries[0])
                        val transaction = parentFragmentManager.beginTransaction() // 또는 requireActivity().supportFragmentManager
                        transaction.replace(R.id.fragment_container, fragment) // fragment_container는 Fragment가 표시될 뷰의 ID입니다.
                        transaction.addToBackStack(null) // 뒤로 가기 스택에 추가
                        transaction.commit()
                    } else {
                        // 일기가 없으면 아무 일도 하지 않음
                        Toast.makeText(requireContext(), "해당 날짜의 일기가 없습니다.", Toast.LENGTH_SHORT).show()
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
