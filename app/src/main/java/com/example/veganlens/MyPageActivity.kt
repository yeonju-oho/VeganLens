package com.example.veganlens

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MyPageActivity : AppCompatActivity() {

    private lateinit var tvNickname: TextView
    private lateinit var tvVeganLevel: TextView
    private lateinit var ivProfileImage: ImageView
    private lateinit var tvIntro: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        tvNickname = findViewById(R.id.tvNickname)
        tvVeganLevel = findViewById(R.id.tvVeganLevel)
        ivProfileImage = findViewById(R.id.ivProfileImage)
        tvIntro = findViewById(R.id.tvIntro)

        // 비건 레벨 수정 클릭 이벤트
        tvVeganLevel.setOnClickListener {
            // 비건 레벨 수정 로직 구현
        }

        // 프로필 이미지 클릭 이벤트
        ivProfileImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1000)
        }

        // 자기소개 클릭 이벤트
        tvIntro.setOnClickListener {
            showEditIntroDialog()
        }
    }

    // 자기소개 수정 다이얼로그 표시
    private fun showEditIntroDialog() {
        val editText = EditText(this).apply {
            hint = "자기소개를 입력하세요"
            setText(tvIntro.text)
        }

        AlertDialog.Builder(this)
            .setTitle("자기소개 수정")
            .setView(editText)
            .setPositiveButton("저장") { dialog, _ ->
                val newIntro = editText.text.toString()
                tvIntro.text = newIntro
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            ivProfileImage.setImageURI(imageUri)
        }
    }
}
