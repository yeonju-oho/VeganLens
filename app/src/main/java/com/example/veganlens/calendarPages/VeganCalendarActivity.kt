package com.example.veganlens

import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class VeganCalendarActivity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var ivAddDiary: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vegan_calendar)

        calendarView = findViewById(R.id.calendarView)
        ivAddDiary = findViewById(R.id.ivAddDiary)

        // 달력 날짜 선택 이벤트
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val intent = Intent(this, VeganDiaryDetailActivity::class.java)
            intent.putExtra("date", "$year-${month + 1}-$dayOfMonth")
            startActivity(intent)
        }

        // 플로팅 버튼 (ImageView) 클릭 이벤트
        ivAddDiary.setOnClickListener {
            val intent = Intent(this, AddVeganDiaryActivity::class.java)
            startActivity(intent)
        }
    }
}
