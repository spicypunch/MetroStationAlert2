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
    }
    
    private fun loadAllStations() {
        viewModelScope.launch {
            _searchScreenState.value = _searchScreenState.value.copy(isLoading = true)
            
            try {
                val stations = getSubwayStationsUseCase()
                _searchScreenState.value = _searchScreenState.value.copy(
                    stations = stations,
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
        searchStations(query)
    }
    
    private fun searchStations(query: String) {
        viewModelScope.launch {
            _searchScreenState.value = _searchScreenState.value.copy(isLoading = true)
            
            try {
                val stations = searchSubwayStationsUseCase(query)
                _searchScreenState.value = _searchScreenState.value.copy(
                    stations = stations,
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
}