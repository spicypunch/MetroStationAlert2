package kr.jm.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kr.jm.data.datastore.UserPreferencesDataStore
import kr.jm.domain.model.AlertSettings
import kr.jm.domain.model.LocationData
import kr.jm.domain.repository.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val userPreferencesDataStore: UserPreferencesDataStore
) : LocationRepository {

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
}