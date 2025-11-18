package kr.jm.domain.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kr.jm.domain.repository.UserPreferencesRepository
import kotlin.test.assertEquals
import org.junit.Before
import org.junit.Test

class GetNotificationContentUseCaseTest {

    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var getNotificationContentUseCase: GetNotificationContentUseCase

    @Before
    fun setUp() {
        userPreferencesRepository = mockk()
        getNotificationContentUseCase = GetNotificationContentUseCase(userPreferencesRepository)
    }

    @Test
    fun `invoke returns notification content flow from repository`() = runTest {
        // Given
        val expectedContent = "곧 도착합니다"
        every { userPreferencesRepository.getNotiContent() } returns flowOf(expectedContent)

        // When
        val result = getNotificationContentUseCase()

        // Then
        result.collect { content ->
            assertEquals(expectedContent, content)
        }
        verify { userPreferencesRepository.getNotiContent() }
    }

    @Test
    fun `invoke returns default notification content`() = runTest {
        // Given
        val defaultContent = "하차 준비해주세요"
        every { userPreferencesRepository.getNotiContent() } returns flowOf(defaultContent)

        // When
        val result = getNotificationContentUseCase()

        // Then
        result.collect { content ->
            assertEquals(defaultContent, content)
        }
        verify { userPreferencesRepository.getNotiContent() }
    }

    @Test
    fun `invoke returns updated notification content`() = runTest {
        // Given
        val content1 = "첫 번째 내용"
        val content2 = "두 번째 내용"
        every { userPreferencesRepository.getNotiContent() } returns flowOf(content1, content2)

        // When
        val result = getNotificationContentUseCase()

        // Then
        val collectedContents = mutableListOf<String>()
        result.collect { content ->
            collectedContents.add(content)
        }
        assertEquals(2, collectedContents.size)
        assertEquals(content1, collectedContents[0])
        assertEquals(content2, collectedContents[1])
        verify { userPreferencesRepository.getNotiContent() }
    }
}
