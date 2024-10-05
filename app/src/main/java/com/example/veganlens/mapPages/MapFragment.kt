package com.example.veganlens.mapPages

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
                2 -> {
                    val incheonDetails = arrayOf(
                        "부평", "구월/소래포구/호구포", "서구",
                        "계양", "주안", "송도/연수","인천공항/을왕리/영종도",
                        "동암/간석","용현/숭의/월미도/신포/동인천/연안부두","강화/옹진"
                    )
                    rightListView.adapter = ArrayAdapter(
                        requireContext(), android.R.layout.simple_list_item_1, incheonDetails
                    )
                }
                3 -> {
                    val gangwonDetails = arrayOf(
                        "춘천/강촌", "원주", "경포대/사천/주문진/정동진",
                        "강릉역/교동/옥계", "용월/정선", "속초/고성","양양",
                        "동해/삼척/태백","평창","홍천/횡성","화천/철원/인제/양구"
                    )
                    rightListView.adapter = ArrayAdapter(
                        requireContext(), android.R.layout.simple_list_item_1, gangwonDetails
                    )
                }
                4 -> {
                    val jejuDetails = arrayOf(
                        "제주공항 서부", "제주공항 동부", "서귀포시/중문/모슬포",
                        "이호테우/하귀/애월/한림/협재", "함덕/김녕/세화", "남원/표성/성산",
                    )
                    rightListView.adapter = ArrayAdapter(
                        requireContext(), android.R.layout.simple_list_item_1, jejuDetails
                    )
                }
                5 -> {
                    val daejeonDetails = arrayOf(
                        "유성구", "중구", "동구",
                        "서구", "대덕구"
                    )
                    rightListView.adapter = ArrayAdapter(
                        requireContext(), android.R.layout.simple_list_item_1, daejeonDetails
                    )
                }
                6 -> {
                    val chunbukDetails = arrayOf(
                        "청주", "충주/수안보", "제천/단양",
                        "진천/음성", "보은/옥천/괴산/증평/영동"
                    )
                    rightListView.adapter = ArrayAdapter(
                        requireContext(), android.R.layout.simple_list_item_1, chunbukDetails
                    )
                }
                7 -> {
                    val chungnamsejongDetails = arrayOf(
                        "천안", "아산", "공주/동학사/세종",
                        "계룡/금산/논산/청양", "예산/홍성","태안/안면도",
                        "서산","당진","보령/대천","서천/부여"
                    )
                    rightListView.adapter = ArrayAdapter(
                        requireContext(), android.R.layout.simple_list_item_1, chungnamsejongDetails
                    )
                }
                8 -> {
                    val busanDetails = arrayOf(
                        "해운대/센텀시티/재송", "송정/기장/정관/오시리아 관광단지", "광안리/수영",
                        "경성대/대연/용호동/문현", "서면/양정/초읍/부산시민공원","남포동/중앙동/태종대/송도/영도",
                        "부산역/범일동/부산진역","연산/토곡","동래/사직/미남/온천장/부산대/구서/서동","사상/엄궁/학장",
                        "덕천/화명/만덕/구포","하단/명지/김해공항/다대포/강서/신호/괴정/지사"
                    )
                    rightListView.adapter = ArrayAdapter(
                        requireContext(), android.R.layout.simple_list_item_1, busanDetails
                    )
                }
                8 -> {
                    val ulsanDetails = arrayOf(
                        "남구/중구", "동구/북구/울주군"
                    )
                    rightListView.adapter = ArrayAdapter(
                        requireContext(), android.R.layout.simple_list_item_1, ulsanDetails
                    )
                }
                9 -> {
                    val gyeongnamDetails = arrayOf(
                        "창원", "마산","진해","김해/장유","양산/밀양","진주","거제/통영/고성",
                        "사천/남해","하동/산청/함양","거창/함안/창녕/합천/의령"
                    )
                    rightListView.adapter = ArrayAdapter(
                        requireContext(), android.R.layout.simple_list_item_1, gyeongnamDetails
                    )
                }
                10 -> {
                    val daeguDetails = arrayOf(
                        "동성로", "동대구역/신천동/시지","대구공항/팔공산/군위","서대구역/평리/비산/동천동",
                        "두류/이월드/본리동/죽전동/대명동/봉덕동","성서/상인동/대곡/달성군"
                    )
                    rightListView.adapter = ArrayAdapter(
                        requireContext(), android.R.layout.simple_list_item_1, daeguDetails
                    )
                }
                11 -> {
                    val gyeongbukDetails = arrayOf(
                        "포항", "경주","구미","경산",
                        "안동","영천/청도","김천/칠곡/성주",
                        "문경/상주/영주/예천/의성/봉화","울진/영덕/청송","울릉도"
                    )
                    rightListView.adapter = ArrayAdapter(
                        requireContext(), android.R.layout.simple_list_item_1, gyeongbukDetails
                    )
                }
                12 -> {
                    val gwangjuDetails = arrayOf(
                        "서구", "동구","남구","양산동",
                        "하남/송정역/광산구","북구"
                    )
                    rightListView.adapter = ArrayAdapter(
                        requireContext(), android.R.layout.simple_list_item_1, gwangjuDetails
                    )
                }
                13 -> {
                    val jeonnamDetails = arrayOf(
                        "여수", "순천","광양","목포",
                        "무안/영암/신안","나주/함평/영광/장성",
                        "담양/곡성/화순/구례","해남/완도/진도/강진/장흥/보성/고흥"
                    )
                    rightListView.adapter = ArrayAdapter(
                        requireContext(), android.R.layout.simple_list_item_1, jeonnamDetails
                    )
                }
                14 -> {
                    val jeonbukDetails = arrayOf(
                        "전주", "군산","익산","남원/임실/순창/무주/진안/장수",
                        "정읍/부안/김제/고창"
                    )
                    rightListView.adapter = ArrayAdapter(
                        requireContext(), android.R.layout.simple_list_item_1, jeonbukDetails
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
            bundle.putString("selectedRestaurantName", selectedRestaurant)
            fragment.arguments = bundle

            // Fragment 전환
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null) // 이전 Fragment로 돌아갈 수 있도록 설정
                .commit()
        }

        // 검색 아이콘 클릭 이벤트 처리
        searchIcon.setOnClickListener {
            // 검색창에서 입력된 텍스트를 가져와서 검색 결과 화면으로 전달
            val searchText = searchEditText.text.toString()
            // 검색 기능 구현 코드 추가 (예: 검색 결과 화면으로 이동)
        }

        // 새로운 액티비티로 이동할 버튼 클릭 이벤트 처리
//        detailButton.setOnClickListener {
//            // FragmentMapListRestaurant로 이동
//            val fragment = FragmentMapListRestaurant() // FragmentMapListRestaurant로 이동하도록 수정
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, fragment)
//                .addToBackStack(null) // 이전 Fragment로 돌아갈 수 있도록 설정
//                .commit()
//        }

        return rootView
    }
}
