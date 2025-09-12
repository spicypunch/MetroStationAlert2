package kr.jm.feature_bookmark

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.jm.domain.repository.OpenApiRepository
import kr.jm.domain.usecase.GetSubwayArrivalTimeUseCase
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val getSubwayArrivalTimeUseCase: GetSubwayArrivalTimeUseCase
): ViewModel() {

    fun getSubwayArrivalTime() {
        viewModelScope.launch {
            val result = getSubwayArrivalTimeUseCase("선릉")
            Log.e("123", result.toString())
        }
    }
}