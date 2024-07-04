package com.example.veganlens.startPages

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
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

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        setupCardView(binding.cardViewNotStarted, R.id.cardTextNotStarted, "아직 채식을 시작하지 않았어!\n채식에 대해 관심이 있어서 비건렌즈에 오게 되었어.")
        setupCardView(binding.cardViewPoloVegetarian, R.id.cardTextPoloVegetarian, "폴로 베지테리언\n나는 🍎🥬🥚🐟🍗만 먹어.")
        setupCardView(binding.cardViewPescoVegetarian, R.id.cardTextPescoVegetarian, "페스코 베지테리언\n나는 🍎🥬🥚🐟만 먹어.")
        setupCardView(binding.cardViewLactoOvoVegetarian, R.id.cardTextLactoOvoVegetarian, "락토 오보 베지테리언\n나는 🍎🥬🥚🥛만 먹어.")
        setupCardView(binding.cardViewOvoVegetarian, R.id.cardTextOvoVegetarian, "오보 베지테리언\n나는 🍎🥬🥚만 먹어.")
        setupCardView(binding.cardViewLactoVegetarian, R.id.cardTextLactoVegetarian, "락토 베지테리언\n나는 🍎🥬🥛만 먹어.")
        setupCardView(binding.cardViewFruitTerrian, R.id.cardTextFruitTerrian, "프루테리언\n나는 🍎만 먹어.")

        binding.buttonNext.setOnClickListener {
            if (selectedVeganLevel != null) {
                sharedPreferences.edit().putString("veganLevel", selectedVeganLevel).apply()
            }
            findNavController().navigate(R.id.action_VeganLevelFragment_to_ResultFragment)
        }
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

    fun getStringBeforeNewLine(input: String): String {
        return input.substringBefore("\n")
    }
    private fun resetSelections() {
        binding.cardViewNotStarted.setBackgroundResource(R.drawable.card_background)
        binding.cardViewPoloVegetarian.setBackgroundResource(R.drawable.card_background)
        binding.cardViewPescoVegetarian.setBackgroundResource(R.drawable.card_background)
        binding.cardViewLactoOvoVegetarian.setBackgroundResource(R.drawable.card_background)
        binding.cardViewOvoVegetarian.setBackgroundResource(R.drawable.card_background)
        binding.cardViewLactoVegetarian.setBackgroundResource(R.drawable.card_background)
        binding.cardViewFruitTerrian.setBackgroundResource(R.drawable.card_background)

        binding.cardViewNotStarted.findViewById<TextView>(R.id.cardTextNotStarted).setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultGreen))
        binding.cardViewPoloVegetarian.findViewById<TextView>(R.id.cardTextPoloVegetarian).setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultGreen))
        binding.cardViewPescoVegetarian.findViewById<TextView>(R.id.cardTextPescoVegetarian).setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultGreen))
        binding.cardViewLactoOvoVegetarian.findViewById<TextView>(R.id.cardTextLactoOvoVegetarian).setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultGreen))
        binding.cardViewOvoVegetarian.findViewById<TextView>(R.id.cardTextOvoVegetarian).setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultGreen))
        binding.cardViewLactoVegetarian.findViewById<TextView>(R.id.cardTextLactoVegetarian).setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultGreen))
        binding.cardViewFruitTerrian.findViewById<TextView>(R.id.cardTextFruitTerrian).setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultGreen))
    }
}