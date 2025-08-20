package kr.jm.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kr.jm.domain.repository.UserPreferencesRepository
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class RemoveBookmarkUseCaseTest {
    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var removeBookmarkUseCase: RemoveBookmarkUseCase

    @Before
    fun setUp() {
        userPreferencesRepository = mockk()
        removeBookmarkUseCase = RemoveBookmarkUseCase(userPreferencesRepository)
    }

    @Test
    fun `북마크 삭제 성공 시 Result_success 반환`() = runTest {
        // Given
        val stationName = "야탑역"
        val expectedResult = Result.success(stationName)
        coEvery { userPreferencesRepository.removeBookmark(stationName) } returns expectedResult

        // When
        val result = removeBookmarkUseCase(stationName)

        // Then
        assertEquals(expectedResult, result)
        coVerify(exactly = 1) { userPreferencesRepository.removeBookmark(stationName) }
    }

    @Test
    fun `북마크 삭제 실패 시 Result_failure 반환`() = runTest {
        // Given
        val stationName = "야탑역"
        val expectedResult = Result.failure<String>(Exception("DataStore error"))
        coEvery { userPreferencesRepository.removeBookmark(stationName) } returns expectedResult

        // When
        val result = removeBookmarkUseCase(stationName)

        // Then
        assertEquals(expectedResult.isFailure, result.isFailure)
        coVerify(exactly = 1) { userPreferencesRepository.removeBookmark(stationName) }
    }
}