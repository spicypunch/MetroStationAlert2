package kr.jm.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.jm.domain.repository.UserPreferencesRepository

class GetBookmarkUseCase(
    private val repository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<Set<String>> = repository.getBookmarks()
}