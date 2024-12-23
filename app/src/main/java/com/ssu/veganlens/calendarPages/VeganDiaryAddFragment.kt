package com.ssu.veganlens.calendarPages

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.ssu.veganlens.R
import com.ssu.veganlens.databinding.FragmentVeganDiaryAddBinding
import com.ssu.veganlens.network.AddDiaryRequest
import com.ssu.veganlens.network.AddDiaryResponse
import com.ssu.veganlens.network.NetworkService
import com.ssu.veganlens.veganLogPages.VeganDiaryFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.ssu.veganlens.network.ImageService
import kotlinx.coroutines.launch
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class VeganDiaryAddFragment : Fragment() {

    private lateinit var binding: FragmentVeganDiaryAddBinding
    private lateinit var sharedPreferences : SharedPreferences
    private var imageUris = mutableListOf<Uri>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVeganDiaryAddBinding.inflate(inflater, container, false)
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
            if (title.isBlank() || content.isBlank())
                return@setOnClickListener

            // saveDiary 호출
            lifecycleScope.launch {
                // 서버에 이미지 올리고, 이미지들의 URL 가져오기.
                val imageUrlStrings = checkUploadImages()
                val diaryRequest = AddDiaryRequest(
                    username = nickname,
                    title = title,
                    content = content,
                    images = imageUrlStrings,
                    isPublic = isPublic
                )
                // 서버로 전송
                saveDiary(diaryRequest)
            }
        }

        // 이미지 뷰 클릭 리스너 설정
        binding.ivPhoto1.setOnClickListener { openGallery() }
        binding.ivPhoto2.setOnClickListener { openGallery() }
        binding.ivPhoto3.setOnClickListener { openGallery() }

        return binding.root
    }

    // 서버에 이미지 올리고, URL 가져오는 코드
    private suspend fun checkUploadImages(): List<String> {
        val returnUri = mutableListOf<String>()

        // 비동기 업로드 후 URL 저장
        for (uri in imageUris) {
            val url = suspendCoroutine<String> { continuation ->
                ImageService.uploadImage(requireContext(), uri) { success, imageUrl ->
                    if (success && imageUrl != null) {
                        continuation.resume(imageUrl)
                    } else {
                        continuation.resumeWithException(Exception("Image upload failed"))
                    }
                }
            }
            returnUri.add(url)
        }

        return returnUri
    }

    // SAVE API CALL
    private fun saveDiary(diaryRequest: AddDiaryRequest) {
        NetworkService.service.addDiray(diaryRequest).enqueue(object : Callback<AddDiaryResponse> {
            override fun onResponse(call: Call<AddDiaryResponse>, response: Response<AddDiaryResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    // 성공적으로 저장
                    Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_SHORT).show()

                    // 작성완료된 페이지로 이동
                    val fragment = VeganDiaryFragment().newInstance(response.body()!!.diary, 1)
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

    //갤러리 열어서 이미지 등록
    private fun openGallery() {
        if (imageUris.size < 3) {
            val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
            imagePickerLauncher.launch(intent)
        }
    }

    // 갤러리에서 이미지 선택을 위한 런처 초기화
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                addImageToImageView(uri)
            }
        }
    }

    // 이미지 뷰에 이미지를 로드한다.
    private fun addImageToImageView(uri: Uri) {
        imageUris.add(uri)
        when (imageUris.size) {
            1 -> Glide.with(this).load(uri).into(binding.ivPhoto1)
            2 -> {
                binding.ivPhoto2.visibility = View.VISIBLE
                Glide.with(this).load(uri).into(binding.ivPhoto2)
            }
            3 -> {
                binding.ivPhoto3.visibility = View.VISIBLE
                Glide.with(this).load(uri).into(binding.ivPhoto3)
            }
        }
    }




}
