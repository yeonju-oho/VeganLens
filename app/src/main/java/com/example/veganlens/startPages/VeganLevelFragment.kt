package com.example.veganlens.startPages

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.veganlens.R
import com.example.veganlens.databinding.FragmentVeganLevelBinding

class VeganLevelFragment : Fragment() {

    private lateinit var binding: FragmentVeganLevelBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var selectedVeganLevel: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVeganLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // SharedPreferences 초기화
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        setupCardView(binding.cardViewNotStarted, R.id.cardTextNotStarted, "아직 채식을 시작하지 않았어!\n채식에 대해 관심이 있어서 비건렌즈에 오게 되었어.")
        setupCardView(binding.cardViewFlex, R.id.cardTextFlex, "플렉시테리언\n나는 식물성 음식을 주로 섭취하지만,\n가끔 고기나 생선을 먹어.")
        setupCardView(binding.cardViewPoloVegetarian, R.id.cardTextPoloVegetarian, "폴로 베지테리언\n나는 🍎🥬🥚🐟🍗만 먹어.")
        setupCardView(binding.cardViewPescoVegetarian, R.id.cardTextPescoVegetarian, "페스코 베지테리언\n나는 🍎🥬🥚🐟만 먹어.")
        setupCardView(binding.cardViewLactoOvoVegetarian, R.id.cardTextLactoOvoVegetarian, "락토 오보 베지테리언\n나는 🍎🥬🥚🥛만 먹어.")
        setupCardView(binding.cardViewOvoVegetarian, R.id.cardTextOvoVegetarian, "오보 베지테리언\n나는 🍎🥬🥚만 먹어.")
        setupCardView(binding.cardViewLactoVegetarian, R.id.cardTextLactoVegetarian, "락토 베지테리언\n나는 🍎🥬🥛만 먹어.")
        setupCardView(binding.cardViewFruitTerrian, R.id.cardTextFruitTerrian, "비건\n나는 🍎🥬만 먹어.")

        binding.buttonNext.setOnClickListener {
            if (selectedVeganLevel != null) {
                // 비건 레벨을 SharedPreferences에 저장
                sharedPreferences.edit().putString("veganLevel", selectedVeganLevel).apply()
                // 결과 화면으로 이동
                findNavController().navigate(R.id.action_VeganLevelFragment_to_ResultFragment)
            } else {
                Toast.makeText(requireContext(), "비건 레벨을 선택해 주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // SharedPreferences에서 닉네임 가져와서 설정
        val nickname = sharedPreferences.getString("nickname", "도토리") // 기본값을 "도토리"로 설정
        binding.tvGreeting.text = "$nickname,\n 넌 어떤 비건을 하는 중이야?"
    }

    private fun setupCardView(cardView: View, textViewId: Int, veganLevel: String) {
        cardView.setBackgroundResource(R.drawable.card_background)
        val textView = cardView.findViewById<TextView>(textViewId)
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultGreen))
        cardView.setOnClickListener {
            toggleSelection(cardView, textView, veganLevel)
        }
    }

    private fun toggleSelection(cardView: View, textView: TextView, veganLevel: String) {
        val isSelected = cardView.tag as? Boolean ?: false
        if (isSelected) {
            cardView.setBackgroundResource(R.drawable.card_background)
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultGreen))
            selectedVeganLevel = null
        } else {
            resetSelections()
            cardView.setBackgroundResource(R.drawable.card_selected_background)
            textView.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            selectedVeganLevel = getStringBeforeNewLine(veganLevel)
        }
        cardView.tag = !isSelected
    }

    private fun resetSelections() {
        binding.cardViewNotStarted.setBackgroundResource(R.drawable.card_background)
        binding.cardViewFlex.setBackgroundResource(R.drawable.card_background)
        binding.cardViewPoloVegetarian.setBackgroundResource(R.drawable.card_background)
        binding.cardViewPescoVegetarian.setBackgroundResource(R.drawable.card_background)
        binding.cardViewLactoOvoVegetarian.setBackgroundResource(R.drawable.card_background)
        binding.cardViewOvoVegetarian.setBackgroundResource(R.drawable.card_background)
        binding.cardViewLactoVegetarian.setBackgroundResource(R.drawable.card_background)
        binding.cardViewFruitTerrian.setBackgroundResource(R.drawable.card_background)

        binding.cardViewNotStarted.findViewById<TextView>(R.id.cardTextNotStarted).setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultGreen))
        binding.cardViewFlex.findViewById<TextView>(R.id.cardTextFlex).setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultGreen))
        binding.cardViewPoloVegetarian.findViewById<TextView>(R.id.cardTextPoloVegetarian).setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultGreen))
        binding.cardViewPescoVegetarian.findViewById<TextView>(R.id.cardTextPescoVegetarian).setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultGreen))
        binding.cardViewLactoOvoVegetarian.findViewById<TextView>(R.id.cardTextLactoOvoVegetarian).setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultGreen))
        binding.cardViewOvoVegetarian.findViewById<TextView>(R.id.cardTextOvoVegetarian).setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultGreen))
        binding.cardViewLactoVegetarian.findViewById<TextView>(R.id.cardTextLactoVegetarian).setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultGreen))
        binding.cardViewFruitTerrian.findViewById<TextView>(R.id.cardTextFruitTerrian).setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultGreen))
    }

    private fun getStringBeforeNewLine(input: String): String {
        return input.substringBefore("\n").trim() // 공백을 제거
    }

}
