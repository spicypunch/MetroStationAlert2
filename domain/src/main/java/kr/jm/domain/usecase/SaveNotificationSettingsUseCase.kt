package kr.jm.domain.usecase

import kr.jm.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class SaveNotificationSettingsUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(title: String, content: String): Result<Unit> {
        return userPreferencesRepository.saveNotificationSettings(title, content)
    }
}