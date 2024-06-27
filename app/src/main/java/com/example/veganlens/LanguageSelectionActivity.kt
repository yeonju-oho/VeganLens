package com.example.veganlens

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.veganlens.databinding.ActivityLanguageSelectionBinding

class LanguageSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLanguageSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences: SharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)

        binding.btnEnglish.setOnClickListener {
            sharedPreferences.edit().putString("language", "en").apply()
            navigateToNicknameActivity()
        }

        binding.btnKorean.setOnClickListener {
            sharedPreferences.edit().putString("language", "ko").apply()
            navigateToNicknameActivity()
        }
    }

    private fun navigateToNicknameActivity() {
        val intent = Intent(this, NicknameActivity::class.java)
        startActivity(intent)
        finish()
    }
}