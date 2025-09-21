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
import kr.jm.domain.model.SubwayArrivalResponse
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
            val arrivalTimeMap: Map<String, SubwayArrivalResponse> = stations
                .map { stationName ->
                    async {
                        try {
                            stationName to getSubwayArrivalTimeUseCase(stationName)
                        } catch (e: Exception) {
                            // 에러 발생 시 해당 역은 제외
                            null
                        }
                    }
                }
                .awaitAll()
                .mapNotNull { it }
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
