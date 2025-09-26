package kr.jm.domain.usecase

import kr.jm.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class ResetAlertStateUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return userPreferencesRepository.resetAlertState()
    }
}