package kr.jm.feature_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kr.jm.domain.model.SubwayStation
import kr.jm.domain.usecase.AddBookmarkUseCase
import kr.jm.domain.usecase.GetSubwayStationsUseCase
import kr.jm.domain.usecase.RemoveBookmarkUseCase
import kr.jm.domain.usecase.SearchSubwayStationsUseCase
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSubwayStationsUseCase: GetSubwayStationsUseCase,
    private val searchSubwayStationsUseCase: SearchSubwayStationsUseCase,
    private val addBookmarkUseCase: AddBookmarkUseCase,
    private val removeBookmarkUseCase: RemoveBookmarkUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchScreenState())
    val uiState: StateFlow<SearchScreenState> = _uiState.asStateFlow()

    private val stationsState: StateFlow<List<SubwayStation>> =
        getSubwayStationsUseCase()
            .catch { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Unknown error occurred"
                )
                emit(emptyList())
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    init {
        loadAllStations()
    }

    private fun loadAllStations() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            stationsState.collectLatest { stations ->
                val currentState = _uiState.value
                val currentLine = currentState.selectedLineName.ifBlank { "전체" }
                val byLine = if (currentLine == "전체") stations else stations.filter { it.lineName == currentLine }

                val finalList = if (currentState.searchQuery.isBlank()) {
                    byLine
                } else {
                    byLine.filter { it.stationName.contains(currentState.searchQuery, ignoreCase = true) }
                }

                _uiState.value = currentState.copy(
                    allStations = stations,
                    filteredStations = finalList,
                    isLoading = false,
                    errorMessage = null
                )
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun searchStations() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val searchStationResult =
                    searchSubwayStationsUseCase(
                        _uiState.value.searchQuery,
                        _uiState.value.filteredStations
                    )
                _uiState.value = _uiState.value.copy(
                    filteredStations = searchStationResult,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Search failed"
                )
            }
        }
    }

    fun onDropdownToggle() {
        _uiState.value = _uiState.value.copy(
            dropDownExpanded = !_uiState.value.dropDownExpanded
        )
    }

    fun onSelectedLineChanged(lineName: String) {
        _uiState.value = _uiState.value.copy(
            selectedLineName = lineName
        )
    }

    fun filterStationName(lineName: String) {
        val filteredStations = _uiState.value.allStations.filter {
            if (lineName == "전체") {
                return@filter true
            } else {
                it.lineName == lineName
//                it.line.contains(lineName, ignoreCase = true)
            }
        }
        _uiState.value = _uiState.value.copy(
            filteredStations = filteredStations
        )
    }

    fun addBookmark(stationName: String) {
        viewModelScope.launch {
            addBookmarkUseCase(stationName)
        }
    }

    fun removeBookmark(stationName: String) {
        viewModelScope.launch {
            removeBookmarkUseCase(stationName)
        }
    }
}
