package com.example.veganlens

import VeganCalendarFragment
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.veganlens.cameraPages.CameraFragment
import com.example.veganlens.mapPages.MapFragment
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
        val iconMap: ImageView = findViewById(R.id.icon_map)
        val iconCamera: ImageView = findViewById(R.id.icon_camera)
        val iconCalendar: ImageView = findViewById(R.id.icon_calendar)
        val iconUser: ImageView = findViewById(R.id.icon_user)
        val iconRecipe: ImageView = findViewById(R.id.icon_recipe)


        // 지도와 레시피 아이콘을 보이지 않도록 설정
        iconMap.visibility = ImageView.GONE  // 또는 INVISIBLE
        iconRecipe.visibility = ImageView.GONE  // 또는 INVISIBLE

        // 아이콘을 비활성화
        iconMap.isEnabled = false
        iconRecipe.isEnabled = false



        iconCamera.setOnClickListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            if (currentFragment is CameraFragment) {
                currentFragment.onCameraIconClicked()
            } else {
                replaceFragment(CameraFragment())
            }
        }

        iconCalendar.setOnClickListener {
            replaceFragment(VeganCalendarFragment())
        }

        iconUser.setOnClickListener {
            replaceFragment(MyPageFragment())
        }

        iconRecipe.setOnClickListener {
            replaceFragment(VeganLogFragment())
        }

        iconMap.setOnClickListener {
            replaceFragment(MapFragment()) // MapFragment로 변경
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}
