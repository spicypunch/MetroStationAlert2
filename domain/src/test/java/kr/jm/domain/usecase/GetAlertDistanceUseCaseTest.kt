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

class GetAlertDistanceUseCaseTest {

    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var getAlertDistanceUseCase: GetAlertDistanceUseCase

    @BeforeEach
    fun setUp() {
        userPreferencesRepository = mockk()
        getAlertDistanceUseCase = GetAlertDistanceUseCase(userPreferencesRepository)
    }

    @Test
    fun `invoke returns distance flow from repository`() = runTest {
        // Given
        val expectedDistance = 1.5f
        every { userPreferencesRepository.getDistance() } returns flowOf(expectedDistance)

        // When
        val result = getAlertDistanceUseCase()

        // Then
        result.collect { distance ->
            assertEquals(expectedDistance, distance)
        }
        verify { userPreferencesRepository.getDistance() }
    }

    @Test
    fun `invoke returns default distance`() = runTest {
        // Given
        val defaultDistance = 0.5f
        every { userPreferencesRepository.getDistance() } returns flowOf(defaultDistance)

        // When
        val result = getAlertDistanceUseCase()

        // Then
        result.collect { distance ->
            assertEquals(defaultDistance, distance)
        }
        verify { userPreferencesRepository.getDistance() }
    }

    @Test
    fun `invoke returns updated distances`() = runTest {
        // Given
        val distances = listOf(1.0f, 2.0f, 3.0f)
        every { userPreferencesRepository.getDistance() } returns flowOf(*distances.toFloatArray())

        // When
        val result = getAlertDistanceUseCase()

        // Then
        val collectedDistances = mutableListOf<Float>()
        result.collect { distance ->
            collectedDistances.add(distance)
        }
        assertEquals(distances, collectedDistances)
        verify { userPreferencesRepository.getDistance() }
    }
}