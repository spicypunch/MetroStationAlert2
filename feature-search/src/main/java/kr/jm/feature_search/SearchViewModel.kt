package kr.jm.feature_search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class SearchViewModel : ViewModel() {
    private var _searchScreenState = mutableStateOf(SearchScreenState())
    val searchScreenState: State<SearchScreenState> = _searchScreenState
}