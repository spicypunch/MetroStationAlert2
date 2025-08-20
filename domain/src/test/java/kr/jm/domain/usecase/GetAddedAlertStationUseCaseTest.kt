package kr.jm.domain.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kr.jm.domain.repository.UserPreferencesRepository
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetAddedAlertStationUseCaseTest {

    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var getAddedAlertStationUseCase: GetAddedAlertStationUseCase

    @Before
    fun setUp() {
        userPreferencesRepository = mockk()
        getAddedAlertStationUseCase = GetAddedAlertStationUseCase(userPreferencesRepository)
    }

    @Test
    fun `알림으로 등록된 역 이름을 Flow로 반환해야 한다`() = runTest {
        // Given
        val expectedStationName = "야탑역"
        val expectedFlow = flowOf(expectedStationName)
        every { userPreferencesRepository.getAddedAlertStation() } returns expectedFlow

        // When
        val resultFlow = getAddedAlertStationUseCase()

        // Then
        assertEquals(expectedStationName, resultFlow.first())
        verify(exactly = 1) { userPreferencesRepository.getAddedAlertStation() }
    }

    @Test
    fun `알림으로 등록된 역이 없으면 null을 포함한 Flow를 반환해야 한다`() = runTest {
        // Given (준비)
        val expectedFlow = flowOf<String?>(null)
        every { userPreferencesRepository.getAddedAlertStation() } returns expectedFlow

        // When (실행)
        val resultFlow = getAddedAlertStationUseCase()

        // Then (검증)
        assertEquals(null, resultFlow.first())
        verify(exactly = 1) { userPreferencesRepository.getAddedAlertStation() }
    }
}
