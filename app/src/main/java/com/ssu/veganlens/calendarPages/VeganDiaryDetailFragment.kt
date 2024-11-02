import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.ssu.veganlens.DiaryDetailFragment
import com.ssu.veganlens.R
import com.ssu.veganlens.databinding.FragmentVeganDiaryDetailBinding
import com.ssu.veganlens.network.AddDiaryRequest
import com.ssu.veganlens.network.AddDiaryResponse
import com.ssu.veganlens.network.NetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VeganDiaryDetailFragment : Fragment() {

    private lateinit var binding: FragmentVeganDiaryDetailBinding
    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVeganDiaryDetailBinding.inflate(inflater, container, false)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        // 현재 날짜 가져오기
        val currentDate = Date()
        // 날짜 포맷 설정
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        binding.tvDiaryDate.text = dateFormat.format(currentDate)

        // 작성완료 버튼 클릭 이벤트
        binding.btnSave.setOnClickListener {
            val nickname = sharedPreferences.getString("nickname", "도토리") ?: "도토리"
            val title = binding.etDiaryTitle.text.toString()
            val content = binding.etDiaryContent.text.toString()
            var isPublic = binding.switchPublic.isChecked

            // 내용 있는지 확인
            if (title.isNullOrBlank() || content.isNullOrBlank())
                return@setOnClickListener

            // 이미지 등록하고 URL 가져오기.
            val imageUrls = checkUploadImages()

            // DiaryRequest 객체 생성
            val diaryRequest = AddDiaryRequest(
                username = nickname,
                title = title,
                content = content,
                images = imageUrls,
                isPublic = isPublic
            )

            // 서버로 전송
            saveDiary(diaryRequest)
        }

        return binding.root
    }

    private fun saveDiary(diaryRequest: AddDiaryRequest) {
        NetworkService.service.addDiray(diaryRequest).enqueue(object : Callback<AddDiaryResponse> {
            override fun onResponse(call: Call<AddDiaryResponse>, response: Response<AddDiaryResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    // 성공적으로 저장
                    Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_SHORT).show()

                    // 작성완료된 페이지로 이동
                    val fragment = DiaryDetailFragment().newInstance(response.body()!!.diary)
                    val transaction = parentFragmentManager.beginTransaction() // 또는 requireActivity().supportFragmentManager
                    transaction.replace(R.id.fragment_container, fragment) // fragment_container는 Fragment가 표시될 뷰의 ID입니다.
                    //transaction.addToBackStack(null) // 뒤로 가기 스택에 추가
                    transaction.commit()
                } else {
                    // 실패한 경우
                    Toast.makeText(requireContext(), "일기 저장 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AddDiaryResponse>, t: Throwable) {
                // 네트워크 또는 서버 오류 처리
                Toast.makeText(requireContext(), "서버 오류 발생", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkUploadImages() : List<String>{
        val imageUrls = mutableListOf<String>()

// TODO: 구현 필요
//        // ivPhoto1이 보여지고 있으면 업로드
//        val photo1Uri: Uri? = getUriFromImageView(ivPhoto1)
//        if (photo1Uri != null) {
//            uploadImage(photo1Uri) { imageUrl ->
//                imageUrls.add(imageUrl)
//                //업로드 처리
//            }
//        }
//
//        // ivPhoto2가 보여지고 있으면 업로드
//        if (ivPhoto2.visibility == View.VISIBLE) {
//            val photo2Uri: Uri? = getUriFromImageView(ivPhoto2)
//            if (photo2Uri != null) {
//                uploadImage(photo2Uri) { imageUrl ->
//                    imageUrls.add(imageUrl)
//                    //업로드 처리
//                }
//            }
//        }

        return imageUrls;
    }
}
