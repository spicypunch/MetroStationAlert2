package kr.jm.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kr.jm.domain.repository.UserPreferencesRepository
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

class AddAlertStationUseCaseTest {

    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var addAlertStationUseCase: AddAlertStationUseCase

    @Before
    fun setUp() {
        userPreferencesRepository = mockk()
        addAlertStationUseCase = AddAlertStationUseCase(userPreferencesRepository)
    }

    @Test
    fun `하차 알림 받을 지하철 역 추가 성공 시 Result_success 반환`() = runTest {
        // Given
        val stationName = "야탑역"
        val expectedResult = Result.success(stationName)
        coEvery { userPreferencesRepository.addAlertStation(stationName) } returns expectedResult

        // When
        val result = addAlertStationUseCase(stationName)

        // Then
        assertEquals(expectedResult, result)
        coVerify(exactly = 1) { userPreferencesRepository.addAlertStation(stationName) }
    }

    @Test
    fun `하차 알림 받을 지하철 역 추가 실패 시 Result_failure 반환`() = runTest {
        // Given
        val stationName = "야탑역"
        val exception = Exception("DataStore error")
        val expectedResult = Result.failure<String>(exception)
        coEvery { userPreferencesRepository.addAlertStation(stationName) } returns expectedResult

        // When
        val result = addAlertStationUseCase(stationName)

        // Then
        assertEquals(expectedResult.isFailure, result.isFailure)
        coVerify(exactly = 1) { userPreferencesRepository.addAlertStation(stationName) }
    }
}