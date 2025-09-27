package kr.jm.domain.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kr.jm.domain.repository.UserPreferencesRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class GetNotificationTitleUseCaseTest {

    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var getNotificationTitleUseCase: GetNotificationTitleUseCase

    @BeforeEach
    fun setUp() {
        userPreferencesRepository = mockk()
        getNotificationTitleUseCase = GetNotificationTitleUseCase(userPreferencesRepository)
    }

    @Test
    fun `invoke returns notification title flow from repository`() = runTest {
        // Given
        val expectedTitle = "하차 알림"
        every { userPreferencesRepository.getNotiTitle() } returns flowOf(expectedTitle)

        // When
        val result = getNotificationTitleUseCase()

        // Then
        result.collect { title ->
            assertEquals(expectedTitle, title)
        }
        verify { userPreferencesRepository.getNotiTitle() }
    }

    @Test
    fun `invoke returns default notification title`() = runTest {
        // Given
        val defaultTitle = "지하철 알림"
        every { userPreferencesRepository.getNotiTitle() } returns flowOf(defaultTitle)

        // When
        val result = getNotificationTitleUseCase()

        // Then
        result.collect { title ->
            assertEquals(defaultTitle, title)
        }
        verify { userPreferencesRepository.getNotiTitle() }
    }

    @Test
    fun `invoke returns updated notification title`() = runTest {
        // Given
        val title1 = "첫 번째 제목"
        val title2 = "두 번째 제목"
        every { userPreferencesRepository.getNotiTitle() } returns flowOf(title1, title2)

        // When
        val result = getNotificationTitleUseCase()

        // Then
        val collectedTitles = mutableListOf<String>()
        result.collect { title ->
            collectedTitles.add(title)
        }
        assertEquals(2, collectedTitles.size)
        assertEquals(title1, collectedTitles[0])
        assertEquals(title2, collectedTitles[1])
        verify { userPreferencesRepository.getNotiTitle() }
    }
}