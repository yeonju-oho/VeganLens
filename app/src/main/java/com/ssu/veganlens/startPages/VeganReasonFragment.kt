package com.ssu.veganlens.startPages

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.ssu.veganlens.R
import com.ssu.veganlens.databinding.FragmentVeganReasonBinding

class VeganReasonFragment : Fragment() {

    private lateinit var binding: FragmentVeganReasonBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var cardViews: List<View>
    private lateinit var textViews: List<TextView>
    private var selectedIndex: Int = -1  // 선택된 카드뷰 인덱스를 저장하는 변수

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVeganReasonBinding.inflate(inflater, container, false)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 각 카드뷰와 텍스트뷰를 리스트로 저장
        cardViews = listOf(
            binding.cardViewHealth,
            binding.cardViewMeat,
            binding.cardViewAnimal,
            binding.cardViewReligion,
            binding.cardViewEnvironment,
            binding.cardViewWeight,
            binding.cardViewFamine
        )

        textViews = listOf(
            binding.cardTextHealth,
            binding.cardTextMeat,
            binding.cardTextAnimal,
            binding.cardTextReligion,
            binding.cardTextEnvironment,
            binding.cardTextWeight,
            binding.cardTextFamine
        )

        // 카드뷰 설정
        for (i in cardViews.indices) {
            setupCardView(cardViews[i], textViews[i], i)
        }

        // 다음 버튼 설정 (카드뷰가 선택되었을 때만 이동)
        binding.buttonNext.setOnClickListener {
            if (selectedIndex != -1) {
                saveSelectedReasonToSharedPreferences(selectedIndex)
                findNavController().navigate(R.id.action_VeganReasonFragment_to_VeganLevelFragment)
            }
        }

        // SharedPreferences에서 닉네임 가져와서 설정
        val nickname = sharedPreferences.getString("nickname", "도토리") // 기본값을 "도토리"로 설정
        binding.tvGreeting.text = "안녕! $nickname!\n비건을 왜 시작하게 되었어?"
    }

    private fun setupCardView(cardView: View, textView: TextView, index: Int) {
        cardView.setBackgroundResource(R.drawable.card_background)
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultGreen))

        cardView.setOnClickListener {
            selectCardView(index)
        }
    }

    private fun selectCardView(index: Int) {
        // 이미 선택된 카드가 있다면 그 카드의 선택을 해제
        if (selectedIndex != -1 && selectedIndex != index) {
            resetCardView(selectedIndex)
        }

        // 선택된 카드 설정
        selectedIndex = index
        cardViews[index].setBackgroundResource(R.drawable.card_selected_background)
        textViews[index].setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
    }

    private fun resetCardView(index: Int) {
        cardViews[index].setBackgroundResource(R.drawable.card_background)
        textViews[index].setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultGreen))
    }

    private fun saveSelectedReasonToSharedPreferences(reasonIndex: Int) {
        val reasonCode = when (reasonIndex) {
            0 -> 1  // HEALTH_CONCERNS
            1 -> 2  // ENVIRONMENTAL_HORMONES
            2 -> 3  // LOVE_FOR_ANIMALS
            3 -> 4  // RELIGIOUS_REASONS
            4 -> 5  // ENVIRONMENTAL_CONCERNS
            5 -> 6  // WEIGHT_LOSS
            6 -> 7  // WORLD_HUNGER
            else -> 1  // 기본값은 HEALTH_CONCERNS
        }

        sharedPreferences.edit().putInt("veganReason", reasonCode).apply()
    }
}
