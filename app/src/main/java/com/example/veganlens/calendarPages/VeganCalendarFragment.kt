package com.example.veganlens

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.ImageView
import androidx.fragment.app.Fragment

class VeganCalendarFragment : Fragment() {

    private lateinit var calendarView: CalendarView
    private lateinit var ivAddDiary: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_vegan_calendar, container, false)

        // 뷰 바인딩
        calendarView = view.findViewById(R.id.calendarView)
        ivAddDiary = view.findViewById(R.id.ivAddDiary)

        // 달력 날짜 선택 이벤트
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val intent = Intent(requireContext(), VeganDiaryDetailFragment::class.java)
            intent.putExtra("date", "$year-${month + 1}-$dayOfMonth")
            startActivity(intent)
        }

        // 플로팅 버튼 (ImageView) 클릭 이벤트
        ivAddDiary.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, VeganDiaryDetailFragment())
            transaction.addToBackStack(null) // 뒤로 가기 버튼으로 돌아갈 수 있게 함
            transaction.commit()
        }

        return view
    }
}
