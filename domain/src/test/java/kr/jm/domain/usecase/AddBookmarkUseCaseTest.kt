package kr.jm.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kr.jm.domain.repository.UserPreferencesRepository
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class AddBookmarkUseCaseTest {

    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var addBookmarkUseCase: AddBookmarkUseCase

    @Before
    fun setUp() {
        userPreferencesRepository = mockk()
        addBookmarkUseCase = AddBookmarkUseCase(userPreferencesRepository)
    }

    @Test
    fun `북마크 추가 성공 시 Result_success 반환`() = runTest {
        // Given
        val stationName = "강남역"
        val expectedResult = Result.success(stationName)
        coEvery { userPreferencesRepository.addBookmark(stationName) } returns expectedResult

        // When
        val result = addBookmarkUseCase(stationName)

        // Then
        assertEquals(expectedResult, result)
        coVerify(exactly = 1) { userPreferencesRepository.addBookmark(stationName) }
    }

    @Test
    fun `북마크 추가 실패 시 Result_failure 반환`() = runTest {
        // Given
        val stationName = "강남역"
        val exception = Exception("DataStore error")
        val expectedResult = Result.failure<String>(exception)
        coEvery { userPreferencesRepository.addBookmark(stationName) } returns expectedResult

        // When
        val result = addBookmarkUseCase(stationName)

        // Then
        assertEquals(expectedResult.isFailure, result.isFailure)
        coVerify(exactly = 1) { userPreferencesRepository.addBookmark(stationName) }
    }
}