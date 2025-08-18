package kr.jm.domain.usecase

import kr.jm.domain.repository.UserPreferencesRepository

class AddAlertStationUseCase(
    private val repository: UserPreferencesRepository
) {
    suspend operator fun invoke(stationName: String): Result<String> {
        return repository.addAlertStation(stationName)
    }
}