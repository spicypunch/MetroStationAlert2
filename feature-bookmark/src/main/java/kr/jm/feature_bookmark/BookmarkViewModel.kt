package kr.jm.feature_bookmark
import android.util.Log
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
import kr.jm.domain.repository.OpenApiRepository
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

    private fun getSubwayArrivalTime(stations: Set<String>) {
        viewModelScope.launch {
            val arrivalTimeMap: Map<String, SubwayArrivalResponse> = stations
                .map { stationName ->
                    async {
                        try {
                            stationName to getSubwayArrivalTimeUseCase(stationName)
                        } catch (e: Exception) {
                            Log.e("BookmarkViewModel", "Error getting subway arrival time: $e")
                            null
                        }
                    }
                }
                .awaitAll()
                .mapNotNull { it }
                .toMap()

            _uiState.value = _uiState.value.copy(
                arrivalTimeMap = arrivalTimeMap
            )
        }
    }
}
