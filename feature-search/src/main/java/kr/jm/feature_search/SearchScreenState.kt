package kr.jm.feature_search

import kr.jm.domain.model.SubwayStation

data class SearchScreenState(
    val stations: List<SubwayStation> = emptyList(),
    val searchQuery: String = "",
    val dropDownExpanded: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
