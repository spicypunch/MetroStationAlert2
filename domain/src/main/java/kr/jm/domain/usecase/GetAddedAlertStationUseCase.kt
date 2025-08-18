package kr.jm.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.jm.domain.repository.UserPreferencesRepository

class GetAddedAlertStationUseCase(
    private val repository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<String?> = repository.getAddedAlertStation()
}