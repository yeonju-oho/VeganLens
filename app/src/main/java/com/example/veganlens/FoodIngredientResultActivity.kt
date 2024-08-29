package com.example.veganlens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class FoodIngredientResultActivity : AppCompatActivity() {

    private lateinit var tvResultTitle: TextView
    private lateinit var tvVeganLevel: TextView
    private lateinit var tvReportIssue: TextView
    private lateinit var tvIngredients: TextView
    private lateinit var tvVeganCheck: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_ingredient_result)

        // XML 요소들을 변수에 연결
        tvResultTitle = findViewById(R.id.tvResultTitle)
        tvVeganLevel = findViewById(R.id.tvVeganLevel)
        tvReportIssue = findViewById(R.id.tvReportIssue)
        tvIngredients = findViewById(R.id.tvIngredients)
        tvVeganCheck = findViewById(R.id.tvVeganCheck)

        // 예시 데이터 설정 (실제로는 인텐트나 API 호출 등을 통해 데이터를 받아와야 함)
        tvResultTitle.text = "원재료명을 확인한 결과"
        tvVeganLevel.text = "이 제품은 비건레벨이 먹을 수 있어요!"
        tvReportIssue.text = "수정 제보하기"
        tvIngredients.text = "원액두유, 분리대두단백, 알로로오스, 식염,\n영양강화제, 정제수, 향료 2종"
        tvVeganCheck.text = "원재료 중 계란, 우유 등이 확인되지 않았어요."

        // "수정 제보하기" 클릭 이벤트 처리
        tvReportIssue.setOnClickListener {
            // 여기서 수정 제보하기 기능을 구현
        }
    }
}
