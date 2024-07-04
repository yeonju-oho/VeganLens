package com.example.veganlens.startPages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.veganlens.R
import com.example.veganlens.databinding.FragmentVeganLevelBinding
import com.example.veganlens.databinding.FragmentVeganReasonBinding

class VeganReasonFragment : Fragment() {

    private lateinit var binding: FragmentVeganReasonBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVeganReasonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCardView(binding.cardViewHealth, R.id.cardTextHealth)
        setupCardView(binding.cardViewMeat, R.id.cardTextMeat)
        setupCardView(binding.cardViewAnimal, R.id.cardTextAnimal)
        setupCardView(binding.cardViewReligion, R.id.cardTextReligion)
        setupCardView(binding.cardViewEnvironment, R.id.cardTextEnvironment)
        setupCardView(binding.cardViewWeight, R.id.cardTextWeight)
        setupCardView(binding.cardViewFamine, R.id.cardTextFamine)

        binding.buttonNext.setOnClickListener {
            findNavController().navigate(R.id.action_VeganReasonFragment_to_VeganLevelFragment)
        }
    }

    private fun setupCardView(cardView: View, textViewId: Int) {
        cardView.setBackgroundResource(R.drawable.card_background)
        val textView = cardView.findViewById<TextView>(textViewId)
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultGreen))
        cardView.setOnClickListener {
            toggleSelection(cardView, textView)
        }
    }

    private fun toggleSelection(cardView: View, textView: TextView) {
        val isSelected = cardView.tag as? Boolean ?: false
        if (isSelected) {
            cardView.setBackgroundResource(R.drawable.card_background)
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultGreen))
        } else {
            cardView.setBackgroundResource(R.drawable.card_selected_background)
            textView.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
        }
        cardView.tag = !isSelected
    }
}
