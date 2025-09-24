package kr.jm.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.jm.domain.model.LocationData
import kr.jm.domain.repository.LocationRepository
import javax.inject.Inject

class GetAlertStationLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    operator fun invoke(): Flow<LocationData> {
        return locationRepository.getAlertStationLocation()
    }
}