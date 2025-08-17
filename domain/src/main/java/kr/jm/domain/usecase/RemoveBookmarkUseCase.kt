package kr.jm.domain.usecase

import kr.jm.domain.repository.UserPreferencesRepository

class RemoveBookmarkUseCase(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(stationName: String): Result<String> {
        return userPreferencesRepository.removeBookmark(stationName)
    }
}