package kr.jm.feature_bookmark

import kr.jm.domain.model.SubwayArrivalResponse

data class BookmarkScreenState(
    val bookmarks: Set<String> = emptySet(),
    val arrivalTimeMap: Map<String,SubwayArrivalResponse> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class DirectionArrivalInfo(
    val direction: String, // 방향 (ex: "왕십리행", "송도행")
    val upDownLine: String, // 상행/하행
    val arrivals: List<ArrivalInfo>
)

data class ArrivalInfo(
    val message: String, // 도착 메시지 (ex: "5분후 도착", "전역 출발")
    val trainLineNm: String, // 전체 방향 정보 (ex: "신사행 - 정자방면")
    val receivedTime: String // 정보 수신 시간
)