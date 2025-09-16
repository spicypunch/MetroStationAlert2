package kr.jm.feature_bookmark

import kr.jm.domain.model.SubwayArrivalResponse

data class BookmarkScreenState(
    val bookmarks: Set<String> = emptySet(),
    val arrivalTimeMap: Map<String,SubwayArrivalResponse> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
)