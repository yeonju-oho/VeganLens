package com.example.veganlens

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class VeganVlogDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_vegan_diary_detail.xml 레이아웃을 인플레이트합니다.
        val view = inflater.inflate(R.layout.fragment_vegan_diary_detail, container, false)

        // 로그 추가
        Log.d("VeganVlogDetailFragment", "onCreateView executed")

        // 작성완료 버튼을 찾습니다.
        val btnComplete: Button = view.findViewById(R.id.btn)
        Log.d("VeganVlogDetailFragment", "Button found: ${btnComplete != null}")

        // 버튼 클릭 리스너 설정
        btnComplete.setOnClickListener {
            Log.d("VeganVlogDetailFragment", "Button clicked")

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("알림")
            builder.setMessage("저장완료되었습니다.")
            builder.setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
            builder.show()

            // 버튼 텍스트 변경
            btnComplete.text = "저장완료되었습니다"
            btnComplete.postDelayed({
                btnComplete.text = "작성완료"
            }, 1000)
        }


        return view
    }
}
