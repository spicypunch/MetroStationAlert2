package kr.jm.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.jm.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class GetNotificationTitleUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<String> {
        return userPreferencesRepository.getNotiTitle()
    }
}