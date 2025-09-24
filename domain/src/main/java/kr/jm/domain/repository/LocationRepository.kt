package kr.jm.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.jm.domain.model.AlertSettings
import kr.jm.domain.model.LocationData

interface LocationRepository {
    fun getAlertStationLocation(): Flow<LocationData>
    fun getAlertSettings(): Flow<AlertSettings>
}