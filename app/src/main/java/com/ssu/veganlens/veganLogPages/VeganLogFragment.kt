package com.ssu.veganlens

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.ssu.veganlens.network.Diary
import com.ssu.veganlens.network.DiarySearchResponse
import com.ssu.veganlens.network.NetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VeganLogFragment : Fragment() {

    private lateinit var gridLayout: GridLayout
    private lateinit var nextPageButton: Button
    private lateinit var prevPageButton: Button
    private lateinit var pageIndexTextView: TextView
    private lateinit var bottomPageIndex: LinearLayout
    private var currentPage: Int = 0
    private var postsPerPage: Int = 0  //기본값 설정
    private var diaries: List<Diary> = emptyList() // List<Diary> 변수를 선언

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_veganlog, container, false)

        gridLayout = view.findViewById(R.id.gridLayout)
        nextPageButton = view.findViewById(R.id.nextPageButton)
        prevPageButton = view.findViewById(R.id.prevPageButton)
        pageIndexTextView = view.findViewById(R.id.tvPageIndex)
        bottomPageIndex = view.findViewById(R.id.bottomPageIndex)

        // GridLayout의 행(row)과 열(column) 수를 곱해 postsPerPage 설정
        postsPerPage = gridLayout.columnCount * gridLayout.rowCount

        // 초기 게시물 로드
        loadDiaries() // API 호출

        // 버튼 클릭 리스너 설정
        nextPageButton.setOnClickListener {
            currentPage++
            loadPosts()
        }

        prevPageButton.setOnClickListener {
            if (currentPage > 0) {
                currentPage--
                loadPosts()
            }
        }

        return view
    }

    private fun loadDiaries() {
        NetworkService.service.getAllDiaries().enqueue(object : Callback<DiarySearchResponse> {
            override fun onResponse(call: Call<DiarySearchResponse>, response: Response<DiarySearchResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    diaries = response.body()!!.diaries // List<Diary>에 할당

                    if (diaries.isNotEmpty()) {
                        loadPosts() // 게시물 로드
                    } else {
                        Toast.makeText(requireContext(), "일기가 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "서버에서 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DiarySearchResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun loadPosts() {
        gridLayout.removeAllViews() // 이전 게시물 제거

        // 현재 페이지에 해당하는 게시물만 가져오기
        val start = currentPage * postsPerPage
        val end = minOf(start + postsPerPage, diaries.size)
        val currentDiaries = diaries.subList(start, end)

        for ((index, diary) in currentDiaries.withIndex()) {
            val postView = LayoutInflater.from(requireContext()).inflate(R.layout.item_post, gridLayout, false)
            val imageView = postView.findViewById<ImageView>(R.id.main_image)
            val titleTextView = postView.findViewById<TextView>(R.id.title_text)

            // 이미지를 설정
            if (diary.images.isNotEmpty()) {
                Glide.with(requireContext())
                    .load(diary.images[0])
                    .into(imageView)
            }
            titleTextView.text = diary.title
            gridLayout.addView(postView)
        }

        // 페이지 인덱스 업데이트
        bottomPageIndex.visibility = if (postsPerPage < diaries.size) View.VISIBLE else View.GONE

        pageIndexTextView.text = (currentPage + 1).toString()
        prevPageButton.visibility = if (currentPage > 0) View.VISIBLE else View.INVISIBLE
        nextPageButton.visibility = if (end < diaries.size) View.VISIBLE else View.INVISIBLE
    }
}
