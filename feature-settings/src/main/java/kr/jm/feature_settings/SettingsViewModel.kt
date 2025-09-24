package kr.jm.feature_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kr.jm.domain.usecase.GetAlertDistanceUseCase
import kr.jm.domain.usecase.GetNotificationContentUseCase
import kr.jm.domain.usecase.GetNotificationTitleUseCase
import kr.jm.domain.usecase.SaveAlertDistanceUseCase
import kr.jm.domain.usecase.SaveNotificationSettingsUseCase
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getNotificationTitleUseCase: GetNotificationTitleUseCase,
    private val getNotificationContentUseCase: GetNotificationContentUseCase,
    private val getAlertDistanceUseCase: GetAlertDistanceUseCase,
    private val saveNotificationSettingsUseCase: SaveNotificationSettingsUseCase,
    private val saveAlertDistanceUseCase: SaveAlertDistanceUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsScreenState())
    val uiState: StateFlow<SettingsScreenState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            combine(
                getNotificationTitleUseCase(),
                getNotificationContentUseCase(),
                getAlertDistanceUseCase()
            ) { title, content, distance ->
                _uiState.value.copy(
                    notificationTitle = title,
                    notificationContent = content,
                    alertDistance = distance,
                    isLoading = false
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun onNotificationTitleChanged(title: String) {
        _uiState.value = _uiState.value.copy(notificationTitle = title)
    }

    fun onNotificationContentChanged(content: String) {
        _uiState.value = _uiState.value.copy(notificationContent = content)
    }

    fun onAlertDistanceChanged(distance: Float) {
        _uiState.value = _uiState.value.copy(alertDistance = distance)
    }

    fun onShowBottomSheet() {
        _uiState.value = _uiState.value.copy(showBottomSheet = true)
    }

    fun onHideBottomSheet() {
        _uiState.value = _uiState.value.copy(showBottomSheet = false)
    }

    fun saveNotificationTitle() {
        viewModelScope.launch {
            val currentState = _uiState.value
            saveNotificationSettingsUseCase(
                title = currentState.notificationTitle,
                content = currentState.notificationContent
            ).fold(
                onSuccess = {
                    // Success handled by observing the flow
                },
                onFailure = { exception ->
                    _uiState.value = currentState.copy(
                        errorMessage = exception.message
                    )
                }
            )
        }
    }

    fun saveNotificationContent() {
        viewModelScope.launch {
            val currentState = _uiState.value
            saveNotificationSettingsUseCase(
                title = currentState.notificationTitle,
                content = currentState.notificationContent
            ).fold(
                onSuccess = {
                    // Success handled by observing the flow
                },
                onFailure = { exception ->
                    _uiState.value = currentState.copy(
                        errorMessage = exception.message
                    )
                }
            )
        }
    }

    fun saveAlertDistance() {
        viewModelScope.launch {
            val currentState = _uiState.value
            saveAlertDistanceUseCase(currentState.alertDistance).fold(
                onSuccess = {
                    _uiState.value = currentState.copy(showBottomSheet = false)
                },
                onFailure = { exception ->
                    _uiState.value = currentState.copy(
                        errorMessage = exception.message
                    )
                }
            )
        }
    }

    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}