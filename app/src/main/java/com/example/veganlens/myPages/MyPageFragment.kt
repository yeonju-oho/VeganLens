package com.example.veganlens.myPages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.veganlens.R

class MyPageFragment : Fragment() {

    private lateinit var tvIntro: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 프래그먼트 레이아웃을 inflate합니다.
        val view = inflater.inflate(R.layout.fragment_mypage, container, false)

        // 자기소개 TextView를 초기화합니다.
        tvIntro = view.findViewById(R.id.tvIntro)

        // 자기소개 TextView 클릭 시 다이얼로그를 띄워 사용자 입력을 받습니다.
        tvIntro.setOnClickListener {
            showEditIntroDialog()
        }

        return view
    }

    private fun showEditIntroDialog() {
        val editText = EditText(requireContext()).apply {
            hint = "자기소개를 입력하세요"
            setText(tvIntro.text) // 기존의 자기소개를 기본 값으로 설정합니다.
        }

        AlertDialog.Builder(requireContext())
            .setTitle("자기소개 수정")
            .setView(editText)
            .setPositiveButton("저장") { dialog, _ ->
                val newIntro = editText.text.toString()
                tvIntro.text = newIntro // 사용자가 입력한 텍스트를 TextView에 반영합니다.
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
