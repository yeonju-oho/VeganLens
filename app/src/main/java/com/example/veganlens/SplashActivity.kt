package com.example.veganlens

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager // 추가
import com.example.veganlens.startPages.RegistrationActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // SharedPreferences 초기화
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        // 2초 동안 스플래쉬 화면을 보여주고 LanguageSelectionActivity로 이동
        Handler(Looper.getMainLooper()).postDelayed({

            //TODO: *** 테스트용. 회원가입 하고싶은 경우 주석 해제 ***
            this.clearSharedPreferences();

            // SharedPreferences에서 회원가입 여부 확인
            val isRegistered = sharedPreferences.getBoolean("isRegistered", false)

            // 회원가입 여부에 따라 화면 전환
            if (isRegistered) {
                // 회원가입이 완료된 경우 메인 화면으로 이동
                navigateToMainScreen()
            } else {
                // 회원가입이 완료되지 않은 경우 회원가입 화면으로 이동
                navigateToSignUpScreen()
            }
        }, 2000)
    }

    // 메인 화면으로 이동하는 함수
    private fun navigateToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // 현재 액티비티 종료
    }

    // 회원가입 화면으로 이동하는 함수
    private fun navigateToSignUpScreen() {
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
        finish()
    }

    // SharedPreferences 초기화 하는 함수
    private fun clearSharedPreferences() {
        try {
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
