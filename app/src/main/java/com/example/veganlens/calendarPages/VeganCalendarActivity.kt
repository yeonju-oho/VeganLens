package com.example.veganlens

import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar

class VeganCalendarActivity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var fabAddDiary: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vegan_calendar)

        calendarView = findViewById(R.id.calendarView)
        fabAddDiary = findViewById(R.id.fabAddDiary)

        // 오늘 날짜로 달력 초기화
        val today = Calendar.getInstance().timeInMillis
        calendarView.date = today

        // 달력 날짜 선택 이벤트
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val intent = Intent(this, VeganDiaryDetailActivity::class.java)
            intent.putExtra("date", "$year-${month + 1}-$dayOfMonth")
            startActivity(intent)
        }

        // 플로팅 버튼 클릭 이벤트
        fabAddDiary.setOnClickListener {
            val intent = Intent(this, AddVeganDiaryActivity::class.java)
            startActivity(intent)
        }
    }
}
