package com.example.veganlens.startPages

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.veganlens.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 2초 동안 스플래쉬 화면을 보여주고 LanguageSelectionActivity로 이동
        Handler(Looper.getMainLooper()).postDelayed({

            //TODO: 여기서 첫 시작이면 선택화면, 아니면 사진 찍는 페이지로 이동되도록 해야 함
            startActivity(Intent(this, RegistrationActivity::class.java))
            finish()
        }, 2000)
    }
}