package com.example.veganlens.veganlog

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.veganlens.R
import com.example.veganlens.VeganDiaryActivity

class VeganLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veganlog)  // activity_vegan_log.xml과 연결

        // Today's Vlog 이미지뷰에 클릭 리스너 추가
        val vlogImageView = findViewById<ImageView>(R.id.vlogImageView)
        vlogImageView.setOnClickListener {
            // VeganDiaryActivity로 전환하는 인텐트 생성
            val intent = Intent(this, VeganDiaryActivity::class.java)
            startActivity(intent)
        }
    }
}
