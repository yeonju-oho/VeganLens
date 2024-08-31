package com.example.veganlens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class VeganVlogDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_vegan_vlog_detail.xml 레이아웃을 인플레이트합니다.
        val view = inflater.inflate(R.layout.fragment_vegan_diary_detail, container, false)

        // 작성완료 버튼을 찾습니다.
        val btnComplete: Button = view.findViewById(R.id.btnComplete)

        // 버튼 클릭 리스너 설정
        btnComplete.setOnClickListener {
            // "저장 중입니다." 팝업을 띄웁니다.
            Toast.makeText(requireContext(), "저장 중입니다.", Toast.LENGTH_SHORT).show()

            // 여기서 필요한 추가 작업을 수행할 수 있습니다.
            // 예: 데이터 저장 로직 추가
        }

        return view
    }
}
