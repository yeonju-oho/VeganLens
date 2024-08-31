/*
package com.example.veganlens

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class VeganDiaryDetailActivity : AppCompatActivity() {

    private lateinit var tvDiaryDate: TextView
    private lateinit var tvDiaryContent: TextView
    private lateinit var ivPhoto1: ImageView
    private lateinit var ivPhoto2: ImageView
    private lateinit var ivPhoto3: ImageView
    private lateinit var ivPhoto4: ImageView
    private lateinit var ivPhoto5: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_vegan_diary_detail)

        tvDiaryDate = findViewById(R.id.tvDiaryDate)
        tvDiaryContent = findViewById(R.id.tvDiaryContent)
        ivPhoto1 = findViewById(R.id.ivPhoto1)
        ivPhoto2 = findViewById(R.id.ivPhoto2)
        ivPhoto3 = findViewById(R.id.ivPhoto3)
        ivPhoto4 = findViewById(R.id.ivPhoto4)
        ivPhoto5 = findViewById(R.id.ivPhoto5)

        // 인텐트로 전달된 데이터를 받아와서 UI에 표시
        val date = intent.getStringExtra("date")
        val content = intent.getStringExtra("content")
        val photos = intent.getParcelableArrayListExtra<Uri>("photos")

        // 날짜와 내용 표시
        tvDiaryDate.text = date
        tvDiaryContent.text = content

        // 사진 표시
        photos?.let {
            val imageViews = listOf(ivPhoto1, ivPhoto2, ivPhoto3, ivPhoto4, ivPhoto5)
            for (i in it.indices) {
                imageViews[i].setImageURI(it[i])
                imageViews[i].visibility = ImageView.VISIBLE
            }
        }
    }
}
*/
