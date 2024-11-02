package com.ssu.veganlens.veganLogPages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ssu.veganlens.R
import com.ssu.veganlens.network.Diary
import java.text.SimpleDateFormat
import java.util.Locale

class VeganDiaryFragment : Fragment() {

    private lateinit var diaryTitleTextView: TextView
    private lateinit var diaryContentTextView: TextView
    private lateinit var userNameTextView: TextView
    private lateinit var publicTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var userProfileImageView: ImageView
    private lateinit var diaryImageView: ImageView

    // Diary 객체를 받아오는 함수
    fun newInstance(diary: Diary): VeganDiaryFragment {
        val fragment = VeganDiaryFragment()
        val args = Bundle()
        args.putString("title", diary.title)
        args.putString("content", diary.content)
        args.putString("username", diary.username)
        args.putString("isPublic", if (diary.isPublic) "(공개)" else "(비공개)")
        args.putString("publishedAt", SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(diary.publishedAt))
        args.putStringArrayList("images", ArrayList(diary.images))
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vegan_diary, container, false)

        // 뷰 연결
        diaryTitleTextView = view.findViewById(R.id.diaryTitle)
        diaryContentTextView = view.findViewById(R.id.diaryContext)
        userNameTextView = view.findViewById(R.id.diaryUsername)
        publicTextView = view.findViewById(R.id.diaryPublic)
        dateTextView = view.findViewById(R.id.diaryDate)
        userProfileImageView = view.findViewById(R.id.diaryUserPic)
        diaryImageView = view.findViewById(R.id.diaryImageView)

        // Arguments에서 데이터 가져오기
        arguments?.let {
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

        //userpic은 따로 가져와야 할듯?

        return view
    }
}
