package kr.jm.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kr.jm.data.datastore.UserPreferencesDataStore
import kr.jm.domain.model.AlertSettings
import kr.jm.domain.model.LocationData
import kr.jm.domain.repository.LocationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val userPreferencesDataStore: UserPreferencesDataStore
) : LocationRepository {

    private val _alertState = MutableStateFlow(true)

    override fun getAlertStationLocation(): Flow<LocationData> {
        return combine(
            userPreferencesDataStore.getLatitude(),
            userPreferencesDataStore.getLongitude()
        ) { latitude, longitude ->
            LocationData(
                latitude = latitude,
                longitude = longitude
            )
        }
    }

    override fun getAlertSettings(): Flow<AlertSettings> {
        return combine(
            userPreferencesDataStore.getDistance(),
            userPreferencesDataStore.getNotiTitle(),
            userPreferencesDataStore.getNotiContent()
        ) { distance, title, content ->
            AlertSettings(
                alertDistance = distance,
                notiTitle = title,
                notiContent = content
            )
        }
    }

    override fun getAlertState(): Flow<Boolean> {
        return _alertState
    }

    override suspend fun reactivateAlert() {
        _alertState.value = true
    }

    fun setAlertState(isActive: Boolean) {
        _alertState.value = isActive
    }
}