package kr.jm.domain.usecase

import kr.jm.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class SaveAlertDistanceUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(distance: Float): Result<Unit> {
        return userPreferencesRepository.saveDistance(distance)
    }
}