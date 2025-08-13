package kr.jm.feature_search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.jm.domain.usecase.GetSubwayStationsUseCase
import kr.jm.domain.usecase.SearchSubwayStationsUseCase
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSubwayStationsUseCase: GetSubwayStationsUseCase,
    private val searchSubwayStationsUseCase: SearchSubwayStationsUseCase
) : ViewModel() {

    private val _searchScreenState = mutableStateOf(SearchScreenState())
    val searchScreenState: State<SearchScreenState> = _searchScreenState

    init {
        loadAllStations()
        filterStationName("전체")
    }

    private fun loadAllStations() {
        viewModelScope.launch {
            _searchScreenState.value = _searchScreenState.value.copy(isLoading = true)

            try {
                val stations = getSubwayStationsUseCase()
                _searchScreenState.value = _searchScreenState.value.copy(
                    allStations = stations,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _searchScreenState.value = _searchScreenState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchScreenState.value = _searchScreenState.value.copy(searchQuery = query)
    }

    fun searchStations() {
        viewModelScope.launch {
            _searchScreenState.value = _searchScreenState.value.copy(isLoading = true)

            try {
                val searchStationResult =
                    searchSubwayStationsUseCase(_searchScreenState.value.searchQuery, _searchScreenState.value.filteredStations)
                _searchScreenState.value = _searchScreenState.value.copy(
                    filteredStations = searchStationResult,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _searchScreenState.value = _searchScreenState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Search failed"
                )
            }
        }
    }

    fun onDropdownToggle() {
        _searchScreenState.value = _searchScreenState.value.copy(
            dropDownExpanded = !_searchScreenState.value.dropDownExpanded
        )
    }

    fun onSelectedLineChanged(lineName: String) {
        _searchScreenState.value = _searchScreenState.value.copy(
            selectedLineName = lineName
        )
    }

    fun filterStationName(lineName: String) {
        val filteredStations = _searchScreenState.value.allStations.filter {
            if (lineName == "전체") {
                return@filter true
            } else {
                it.line == lineName
//                it.line.contains(lineName, ignoreCase = true)
            }
        }
        _searchScreenState.value = _searchScreenState.value.copy(
            filteredStations = filteredStations
        )
    }
}