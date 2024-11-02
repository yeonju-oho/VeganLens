package com.ssu.veganlens.cameraPages

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.ssu.veganlens.R

class FoodIngredientResultFragment : DialogFragment() {

    private lateinit var tvIngredients: TextView
    private lateinit var tvVeganLevel: TextView

    private var ingredients: String? = null
    private var veganTypes: String? = null

    //여기서 UI 디자인 수정 가능
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        return dialog
    }

    override fun onResume() {
        super.onResume()

        // 꼭 DialogFragment 클래스에서 선언하지 않아도 된다.
        val windowManager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        // x,y 크기 조절을 위함
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        params?.width = (size.x * 0.9).toInt()
        params?.height = (size.y * 0.7).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_food_ingredient_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var btnReportIssue: Button
        btnReportIssue = view.findViewById(R.id.btnReportIssue)
        btnReportIssue.visibility = ImageView.GONE
        btnReportIssue.isEnabled = false


        tvIngredients = view.findViewById(R.id.tvIngredients)
        tvVeganLevel = view.findViewById(R.id.tvVeganLevel)

        ingredients?.let {
            tvIngredients.text = it
        }
        veganTypes?.let {
            tvVeganLevel.text = it
        }

        val modifyRequestButton: Button = view.findViewById(R.id.btnReportIssue)
        modifyRequestButton.setOnClickListener {
            // Handle modify request action
        }

        val closeButton: Button = view.findViewById(R.id.btnClose)
        closeButton.setOnClickListener {
            dismiss() // DialogFragment를 닫습니다.
        }
    }

    @SuppressLint("SetTextI18n")
    fun setContent(ingredients: String, veganTypes: String) {
        this.ingredients = ingredients
        this.veganTypes = veganTypes

        // UI가 이미 초기화된 경우에는 바로 설정
        if (::tvIngredients.isInitialized && ::tvVeganLevel.isInitialized) {
            tvVeganLevel.text = veganTypes
            tvIngredients.text = ingredients
        }
    }
}

