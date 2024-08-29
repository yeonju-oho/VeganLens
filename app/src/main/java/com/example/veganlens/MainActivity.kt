package com.example.veganlens

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.veganlens.cameraPages.CameraFragment
import com.example.veganlens.myPages.MyPageFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBottomMenu()

        // 기본으로 표시할 프래그먼트 설정
        replaceFragment(CameraFragment())
    }

    private fun setupBottomMenu() {
        val iconCamera: ImageView = findViewById(R.id.icon_camera)
        val iconCalendar: ImageView = findViewById(R.id.icon_calendar)
        val iconUser: ImageView = findViewById(R.id.icon_user)
        val iconRecipe: ImageView = findViewById(R.id.icon_recipe)

        iconCamera.setOnClickListener {
            replaceFragment(CameraFragment())
        }

        iconCalendar.setOnClickListener {
            replaceFragment(VeganCalendarFragment())
        }

        iconUser.setOnClickListener {
            replaceFragment(MyPageFragment())
        }

        iconRecipe.setOnClickListener {
            replaceFragment(VeganDiaryFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}
