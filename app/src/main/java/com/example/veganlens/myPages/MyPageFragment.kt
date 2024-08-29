package com.example.veganlens.myPages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.veganlens.R

class MyPageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 프래그먼트 레이아웃을 inflate합니다.
        val view = inflater.inflate(R.layout.fragment_mypage, container, false)

        return view
    }

}
