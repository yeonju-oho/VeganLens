package com.example.veganlens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit

class VeganLogFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_veganlog, container, false)

        // vlog_image에 대한 클릭 리스너 설정
        val vlogImage: ImageView = view.findViewById(R.id.vlog_image)
        vlogImage.setOnClickListener {
            // VeganDiaryFragment로 대체하여 화면 전환
//            parentFragmentManager.commit {
//                replace(R.id.fragment_container, VeganDiaryFragment())
//                addToBackStack(null) // 뒤로 가기 기능을 위해 백스택에 추가
//            }
        }

        return view
    }
}
