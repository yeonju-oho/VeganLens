package com.ssu.veganlens.mapPages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.ssu.veganlens.R
import com.ssu.veganlens.databinding.FragmentMapListRestaurantBinding

class FragmentMapListRestaurant : Fragment() {

    // 식당 목록 데이터 (이름, 이미지 리소스만 남김)
    private val restaurantList = listOf(
        Restaurant("달리아 다이닝", R.drawable.map1),
        Restaurant("노르디", R.drawable.map2),
        Restaurant("음 이터리", R.drawable.map3)
    )

    private var binding: FragmentMapListRestaurantBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 바인딩 초기화
        binding = FragmentMapListRestaurantBinding.inflate(inflater, container, false)

        // 첫 번째 식당 이미지 설정
        val detailImageView1: ImageView = binding!!.detailImageView1
        val restaurant1 = restaurantList[0]
        detailImageView1.setImageResource(restaurant1.imageResId)

        // 두 번째 식당 이미지 설정
        val detailImageView2: ImageView = binding!!.detailImageView2
        val restaurant2 = restaurantList[1]
        detailImageView2.setImageResource(restaurant2.imageResId)

        // 세 번째 식당 이미지 설정
        val detailImageView3: ImageView = binding!!.detailImageView3
        val restaurant3 = restaurantList[2]
        detailImageView3.setImageResource(restaurant3.imageResId)

        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}


// Restaurant 클래스는 이름과 단일 이미지 리소스 ID만 가집니다.
data class Restaurant(val name: String, val imageResId: Int)
