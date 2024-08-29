package com.example.veganlens.startPages

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.veganlens.cameraPages.CameraFragment
import com.example.veganlens.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        // 닉네임과 비건 단계를 가져와서 바인딩합니다.
        val nickname = sharedPreferences.getString("nickname", "도토리")
        val veganLevel = sharedPreferences.getString("veganLevel", "폴로")

        binding.textViewNickname.text = nickname
        binding.textViewVeganLevel.text = veganLevel

        binding.buttonFinish.setOnClickListener {
            // 메인 화면으로 이동하는 코드 추가
            val intent = Intent(requireContext(), CameraFragment::class.java)
            startActivity(intent)
            requireActivity().finish() // 현재 액티비티를 종료하여 뒤로 가기 버튼 누를 때 다시 이 화면이 나오지 않도록 함
        }

        return binding.root
    }
}
