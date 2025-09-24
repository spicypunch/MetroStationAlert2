package kr.jm.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.jm.domain.model.AlertSettings
import kr.jm.domain.repository.LocationRepository
import javax.inject.Inject

class GetAlertSettingsUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    operator fun invoke(): Flow<AlertSettings> {
        return locationRepository.getAlertSettings()
    }
}