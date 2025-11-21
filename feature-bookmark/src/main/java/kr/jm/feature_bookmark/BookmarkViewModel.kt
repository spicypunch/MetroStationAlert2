package kr.jm.feature_bookmark
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.jm.domain.model.BookmarkKey
import kr.jm.domain.model.SubwayArrivalResponse
import kr.jm.domain.mapper.SubwayLineMapper
import kr.jm.domain.usecase.GetBookmarkUseCase
import kr.jm.domain.usecase.GetSubwayArrivalTimeUseCase
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val getBookmarkUseCase: GetBookmarkUseCase,
    private val getSubwayArrivalTimeUseCase: GetSubwayArrivalTimeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookmarkScreenState())
    val uiState: StateFlow<BookmarkScreenState> = _uiState.asStateFlow()

    init {
        getBookmarks()
    }

    private fun getBookmarks() {
        viewModelScope.launch {
            getBookmarkUseCase().collect { bookmarks ->
                _uiState.value = _uiState.value.copy(
                    bookmarks = bookmarks
                )
                if (bookmarks.isNotEmpty()) {
                    getSubwayArrivalTime(bookmarks)
                }
            }
        }
    }

    fun refreshArrivalInfo() {
        val bookmarks = _uiState.value.bookmarks
        if (bookmarks.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getSubwayArrivalTime(bookmarks)
        }
    }

    private fun getSubwayArrivalTime(stations: Set<String>) {
        viewModelScope.launch {
            // 1. 북마크 키 파싱
            val bookmarkKeys = stations.map { BookmarkKey.parse(it) }
            
            // 2. stationName으로 그룹화하여 중복 API 호출 방지
            val groupedByStation = bookmarkKeys.groupBy { it.stationName }
            
            val arrivalTimeMap: Map<String, SubwayArrivalResponse> = groupedByStation
                .map { (stationName, keys) ->
                    async {
                        try {
                            // 3. API 호출 (stationName으로)
                            val response = getSubwayArrivalTimeUseCase(stationName)
                            
                            // 4. 각 북마크 키에 대해 필터링된 응답 생성
                            keys.mapNotNull { key ->
                                val subwayId = SubwayLineMapper.getSubwayId(key.lineName)
                                val filteredResponse = if (subwayId != null) {
                                    // 특정 노선만 필터링
                                    response.copy(
                                        realtimeArrivalList = response.realtimeArrivalList.filter { 
                                            it.subwayId == subwayId 
                                        }
                                    )
                                } else {
                                    // 전체 노선 또는 알 수 없는 노선
                                    response
                                }
                                key.toKey() to filteredResponse
                            }
                        } catch (e: Exception) {
                            emptyList()
                        }
                    }
                }
                .awaitAll()
                .flatten()
                .toMap()

            _uiState.value = _uiState.value.copy(
                arrivalTimeMap = arrivalTimeMap,
                isLoading = false
            )
        }
    }

    fun processDirectionArrivalInfo(response: SubwayArrivalResponse): List<DirectionArrivalInfo> {
        return response.realtimeArrivalList
            .groupBy { it.updnLine } // 상행/하행으로 그룹핑
            .map { (updownLine, arrivals) ->
                val direction = arrivals.firstOrNull()?.trainLineNm?.split(" - ")?.firstOrNull() ?: "방향 정보 없음"
                val arrivalInfoList = arrivals.map { arrival ->
                    ArrivalInfo(
                        message = arrival.arvlMsg2,
                        trainLineNm = arrival.trainLineNm,
                        receivedTime = arrival.recptnDt
                    )
                }
                DirectionArrivalInfo(
                    direction = direction,
                    upDownLine = updownLine,
                    arrivals = arrivalInfoList
                )
            }
    }
}
