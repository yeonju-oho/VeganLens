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
    private lateinit var cardViews: List<View>
    private lateinit var textViews: List<TextView>
    private lateinit var isSelectedArray: BooleanArray

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVeganReasonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        isSelectedArray = BooleanArray(cardViews.size)

        // 카드뷰 설정
        for (i in cardViews.indices) {
            setupCardView(cardViews[i], textViews[i], i)
        }

        // 다음 버튼 설정
        binding.buttonNext.setOnClickListener {
            if (isSelectedArray.any { it }) {
                findNavController().navigate(R.id.action_VeganReasonFragment_to_VeganLevelFragment)
            }
        }
    }

    private fun setupCardView(cardView: View, textView: TextView, index: Int) {
        cardView.setBackgroundResource(R.drawable.card_background)
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultGreen))
        cardView.setOnClickListener {
            toggleSelection(cardView, textView, index)
        }
    }

    private fun toggleSelection(cardView: View, textView: TextView, index: Int) {
        isSelectedArray[index] = !isSelectedArray[index]

        if (isSelectedArray[index]) {
            cardView.setBackgroundResource(R.drawable.card_selected_background)
            textView.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
        } else {
            cardView.setBackgroundResource(R.drawable.card_background)
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.defaultGreen))
        }
    }
}
