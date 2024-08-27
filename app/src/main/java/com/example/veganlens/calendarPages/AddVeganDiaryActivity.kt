package com.example.veganlens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class AddVeganDiaryActivity : AppCompatActivity() {

    private lateinit var ivAddPhoto: ImageView
    private lateinit var etDiaryContent: EditText
    private lateinit var btnAddTag: Button
    private lateinit var btnComplete: Button
    private lateinit var etTagInput: EditText
    private lateinit var tagContainer: LinearLayout

    private val selectedImages = mutableListOf<Uri>()
    private val tags = mutableListOf<String>()

    // 갤러리에서 사진 선택을 위한 launcher
    private val pickImagesLauncher = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri>? ->
        if (uris != null) {
            if (uris.size > 5) {
                Toast.makeText(this, "최대 5장까지만 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
            } else {
                selectedImages.clear()
                selectedImages.addAll(uris)
                updateImageView()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vegan_diary)

        ivAddPhoto = findViewById(R.id.ivAddPhoto)
        etDiaryContent = findViewById(R.id.etDiaryContent)
        btnAddTag = findViewById(R.id.btnAddTag)
        btnComplete = findViewById(R.id.btnComplete)
        etTagInput = findViewById(R.id.etTagInput)
        tagContainer = findViewById(R.id.tagContainer)

        // 사진 등록 클릭 이벤트
        ivAddPhoto.setOnClickListener {
            checkGalleryPermissionAndPickImages()
        }

        // 태그 추가 클릭 이벤트
        btnAddTag.setOnClickListener {
            addTag()
        }

        // 작성 완료 버튼 클릭 이벤트
        btnComplete.setOnClickListener {
            // 비건 일기 저장 로직 구현
        }
    }

    private fun checkGalleryPermissionAndPickImages() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            pickImagesFromGallery()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
        }
    }

    private fun pickImagesFromGallery() {
        pickImagesLauncher.launch("image/*")
    }

    private fun updateImageView() {
        // 선택된 이미지들을 보여주기 위한 로직
        if (selectedImages.isNotEmpty()) {
            ivAddPhoto.setImageURI(selectedImages[0])
            // 추가적으로 다른 ImageView들을 활용해서 최대 5장까지 보여줄 수 있음.
        }
    }

    private fun addTag() {
        val tagText = etTagInput.text.toString().trim()
        if (tagText.isNotEmpty() && tags.size < 5) {
            tags.add(tagText)
            etTagInput.text.clear()
            displayTags()
        } else {
            Toast.makeText(this, "태그는 최대 5개까지 추가할 수 있습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayTags() {
        tagContainer.removeAllViews()
        for (tag in tags) {
            val tagView = createTagView(tag)
            tagContainer.addView(tagView)
        }
    }

    private fun createTagView(tag: String): TextView {
        val tagView = TextView(this)
        tagView.text = "#$tag"
        tagView.setPadding(16, 8, 16, 8)
        tagView.setBackgroundResource(R.drawable.tag_background) // 태그 배경 drawable 설정함.
        tagView.setTextColor(ContextCompat.getColor(this, R.color.white))

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 8, 8, 8)
        tagView.layoutParams = layoutParams

        tagView.setOnClickListener {
            tags.remove(tag)
            displayTags()
        }

        return tagView
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1000 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImagesFromGallery()
        } else {
            Toast.makeText(this, "갤러리 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }
}
