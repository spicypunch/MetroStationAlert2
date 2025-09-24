package kr.jm.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.jm.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class GetAlertDistanceUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<Float> {
        return userPreferencesRepository.getDistance()
    }
}