package kr.jm.domain.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kr.jm.domain.repository.UserPreferencesRepository
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

class GetBookmarkUseCaseTest {

    private lateinit var usePreferenceRepository: UserPreferencesRepository
    private lateinit var getBookmarkUseCase: GetBookmarkUseCase

    @Before
    fun setUp() {
        usePreferenceRepository = mockk()
        getBookmarkUseCase = GetBookmarkUseCase(usePreferenceRepository)
    }

    @Test
    fun `북마크에 동록된 역 이름을 Flow로 반환해야한다`() = runTest {
        // Given
        val expectedStationName = "야탑역"
        val expectedFlow = flowOf(setOf(expectedStationName))
        every { usePreferenceRepository.getBookmarks() } returns expectedFlow

        // When
        val resultFlow = getBookmarkUseCase()

        // Then
        assertEquals(setOf(expectedStationName), resultFlow.first())
    }

    @Test
    fun `북마크에 등록된 역이 없으면 null을 포함한 Flow를 반환해야 한다`() = runTest {
        // Given
        val expectedFlow = flowOf<Set<String>>(emptySet())
        every { usePreferenceRepository.getBookmarks() } returns expectedFlow

        // When
        val resultFlow = getBookmarkUseCase()

        // Then
        assertEquals(emptySet(), resultFlow.first())
        verify(exactly = 1) { usePreferenceRepository.getBookmarks() }
    }

}