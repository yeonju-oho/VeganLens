package com.example.veganlens

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
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
            // 자기소개 수정 로직 구현
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            ivProfileImage.setImageURI(imageUri)
        }
    }
}
