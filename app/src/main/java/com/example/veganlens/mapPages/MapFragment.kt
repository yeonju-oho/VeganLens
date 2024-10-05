package com.example.veganlens.mapPages

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.veganlens.R

class MapFragment : Fragment() {

    // UI 요소 선언
    private lateinit var leftListView: ListView
    private lateinit var rightListView: ListView
    private lateinit var searchEditText: EditText
    private lateinit var searchIcon: ImageView
    private lateinit var detailButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_map.xml 레이아웃을 inflate
        val rootView = inflater.inflate(R.layout.fragment_map, container, false)

        // UI 요소 초기화
        leftListView = rootView.findViewById(R.id.leftListView)
        rightListView = rootView.findViewById(R.id.rightListView)
        searchEditText = rootView.findViewById(R.id.searchEditText)
        searchIcon = rootView.findViewById(R.id.searchIcon)

        // 왼쪽 목록에 들어갈 지역 데이터
        val regions = arrayOf(
            "서울", "경기", "인천", "강원", "제주", "대전",
            "충북", "충남/세종", "부산", "울산", "경남", "대구",
            "경북", "광주", "전남", "전주/전북"
        )

        // 초기 오른쪽 리스트뷰 내용 (서울 지역 예시)
        val seoulDetails = arrayOf(
            "강남/역삼/삼성/논현", "서초/신사/방배", "잠실/방이",
            "잠실새내/신천/종합운동장","영등포/여의도","신림/서울대/사당/동작",
            "천호/길동/둔촌","화곡/까치산/양천/목동","구로/금천/오류/신도림",
            "연신내/불광/응암","종로/대학로/동묘앞역", "성신여대/성북/월곡",
            "이태원/용산/서울역/명동/회현","동대문/을지로/충무로/신당/약수",
            "회기/고려대/청량리/신설동","장안동/답십리","건대/군자/구의",
            "왕십리/성수/금호","수유/미아","상봉/중랑/면목",
            "태릉/노원/도봉/창동"
        )

        // 왼쪽 리스트뷰 어댑터 설정
        val leftAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1, regions
        )
        leftListView.adapter = leftAdapter

        // 오른쪽 리스트뷰 어댑터 설정 (초기값 서울)
        val rightAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1, seoulDetails
        )
        rightListView.adapter = rightAdapter

        // 왼쪽 리스트뷰 항목 클릭 이벤트 처리
        leftListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> {
                    val seoulDetailsUpdated = arrayOf(
                        "강남/역삼/삼성/논현", "서초/신사/방배", "잠실/방이",
                        "잠실새내/신천/종합운동장","영등포/여의도","신림/서울대/사당/동작",
                        "천호/길동/둔촌","화곡/까치산/양천/목동","구로/금천/오류/신도림",
                        "연신내/불광/응암","종로/대학로/동묘앞역", "성신여대/성북/월곡",
                        "이태원/용산/서울역/명동/회현","동대문/을지로/충무로/신당/약수",
                        "회기/고려대/청량리/신설동","장안동/답십리","건대/군자/구의",
                        "왕십리/성수/금호","수유/미아","상봉/중랑/면목",
                        "태릉/노원/도봉/창동"
                    )
                    rightListView.adapter = ArrayAdapter(
                        requireContext(), android.R.layout.simple_list_item_1, seoulDetailsUpdated
                    )
                }
                1 -> {
                    val gyeonggiDetails = arrayOf(
                        "수원/인계동/나혜석거리", "수원역/구운/행궁/장안구", "수원시청/권선/영통/세류",
                        "안양/평촌/인덕원/과천", "성남/분당/위례", "용인","동탄/화성/오산/병점",
                        "하남/광주","여주/이천","안산 중앙역","안산 고잔/상록수/선부동/월피동",
                        "군포/의왕/금정/산본","시흥","광명","평택/송탄/안성","부천","일산/고양","파주",
                        "김포","의정부","구리","남양주","포천","양주/동두천/연천","양평","가평/청평",
                        "제부도/대부도"
                    )
                    rightListView.adapter = ArrayAdapter(
                        requireContext(), android.R.layout.simple_list_item_1, gyeonggiDetails
                    )
                }
                // 다른 지역에 대한 처리 추가 가능
            }
        }

        // 오른쪽 리스트뷰 항목 클릭 이벤트 처리
        rightListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedRestaurant = rightAdapter.getItem(position)

            // 새로운 Fragment로 이동 (MaplistFragment)
            val fragment = FragmentMapListRestaurant()
            val bundle = Bundle()
            bundle.putString("selectedRestaurantName", selectedRestaurant) // selectedRestaurant?.name 변경
            fragment.arguments = bundle

            findNavController().navigate(R.id.action_MapFragment_to_MapListFragment)
        }

        // 검색 아이콘 클릭 이벤트 처리
        searchIcon.setOnClickListener {
            // 검색창에서 입력된 텍스트를 가져와서 검색 결과 화면으로 전달
            val searchText = searchEditText.text.toString()
            // 검색 기능 구현 코드 추가 (예: 검색 결과 화면으로 이동)
        }

        // 새로운 액티비티로 이동할 버튼 클릭 이벤트 처리
        detailButton.setOnClickListener {
            // MapDetailFragment로 이동
            val intent = Intent(requireContext(), FragmentMapListRestaurant::class.java)
            startActivity(intent)
        }

        return rootView
    }
}
