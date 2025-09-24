package kr.jm.domain.usecase

import kr.jm.domain.repository.UserPreferencesRepository

class AddAlertStationUseCase(
    private val repository: UserPreferencesRepository
) {
    suspend operator fun invoke(stationName: String, latitude: Double, longitude: Double): Result<String> {
        return repository.addAlertStationWithLocation(stationName, latitude, longitude)
    }
}