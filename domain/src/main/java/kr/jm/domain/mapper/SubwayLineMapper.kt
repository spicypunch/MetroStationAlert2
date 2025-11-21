package kr.jm.domain.mapper

object SubwayLineMapper {
    private val lineToSubwayId = mapOf(
        "1호선" to "1001",
        "2호선" to "1002", 
        "3호선" to "1003",
        "4호선" to "1004",
        "5호선" to "1005",
        "6호선" to "1006",
        "7호선" to "1007",
        "8호선" to "1008",
        "9호선" to "1009",
        "분당선" to "1075",
        "수인·분당선" to "1075",
        "신분당선" to "1077",
        "경의중앙선" to "1063",
        "경춘선" to "1067",
        "공항철도" to "1061",
        "자기부상철도" to "1065",
        "우이신설선" to "1092",
        "경강선" to "1085",
        "서해선" to "1093",
        "전체" to null // 마이그레이션용 - 모든 노선 표시
    )
    
    fun getSubwayId(lineName: String): String? {
        return lineToSubwayId[lineName]
    }
    
    fun getLineName(subwayId: String): String? {
        return lineToSubwayId.entries.find { it.value == subwayId }?.key
    }
}
