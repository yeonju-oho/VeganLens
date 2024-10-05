package com.example.veganlens.mapPages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.veganlens.R
import com.example.veganlens.databinding.FragmentMapListRestaurantBinding

class FragmentMapListRestaurant : Fragment() {

    // 식당 목록 데이터를 미리 준비 (이름과 이미지 리소스 ID)
    private val locationData = mapOf(
        "강남" to listOf(
            Restaurant("달리아 다이닝", listOf(R.drawable.map1)),
            Restaurant("노르디", listOf(R.drawable.map2)),
            Restaurant("음이터리", listOf(R.drawable.map3))
        ),
        "서초" to listOf(
            Restaurant("푸드더즈매터", listOf(R.drawable.map4)),
            Restaurant("플랫오", listOf(R.drawable.map5)),
            Restaurant("홀썸", listOf(R.drawable.map6))
        ),
        "잠실" to listOf(
            Restaurant("포리스트키친", listOf(R.drawable.map7)),
            Restaurant("뉴질랜드스토리", listOf(R.drawable.map8)),
            Restaurant("루티드", listOf(R.drawable.map9))
        )
    )

    private var restaurantName: String? = null // nullable로 변경
    private var binding: FragmentMapListRestaurantBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 바인딩 객체 초기화
        binding = FragmentMapListRestaurantBinding.inflate(inflater, container, false)

        // 전달된 식당 이름 가져오기
        arguments?.let {
            restaurantName = it.getString("selectedRestaurantName") // nullable을 안전하게 처리
        }

        // 제목과 설명 세팅
        val titleTextView: TextView = binding!!.titleTextView // null이 아닌 확신이 있을 때 사용
        val descriptionTextView: TextView = binding!!.descriptionTextView

        titleTextView.text = restaurantName ?: "식당"
        descriptionTextView.text = "여기에 ${restaurantName ?: "식당"}의 설명이 들어갑니다." // 안전하게 처리

        // 식당 이미지 설정
        val detailImageView1: ImageView = binding!!.detailImageView1
        val detailImageView2: ImageView = binding!!.detailImageView2
        val detailImageView3: ImageView = binding!!.detailImageView3

        // 이미지 설정
        val restaurantImages = locationData["강남"]?.find { it.name == restaurantName }?.imageResIds
        if (restaurantImages != null && restaurantImages.size >= 3) {
            detailImageView1.setImageResource(restaurantImages[0])
            detailImageView2.setImageResource(restaurantImages[1])
            detailImageView3.setImageResource(restaurantImages[2])
        }

        return binding!!.root // 바인딩된 레이아웃의 루트 뷰 반환
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null // 메모리 누수를 방지하기 위해 바인딩 객체 해제
    }
}
