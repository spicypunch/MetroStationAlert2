package kr.jm.feature_settings

data class SettingsScreenState(
    val notificationTitle: String = "",
    val notificationContent: String = "",
    val alertDistance: Float = 1.0f,
    val showBottomSheet: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)