package com.example.veganlens

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment

class VeganDiaryDetailFragment : Fragment() {

    private lateinit var ivPhoto1: ImageView
    private lateinit var etDiaryContent: EditText
    private val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 레이아웃 인플레이트
        val view = inflater.inflate(R.layout.fragment_vegan_diary_detail, container, false)

        // View 초기화
        ivPhoto1 = view.findViewById(R.id.ivPhoto1)
        etDiaryContent = view.findViewById(R.id.etDiaryContent)

        // 이미지뷰 클릭 리스너 설정
        ivPhoto1.setOnClickListener {
            // 갤러리에서 이미지 선택하는 인텐트 시작
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImageUri)
            ivPhoto1.setImageBitmap(bitmap)
        }
    }
}
