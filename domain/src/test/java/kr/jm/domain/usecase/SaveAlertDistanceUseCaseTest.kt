package kr.jm.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kr.jm.domain.repository.UserPreferencesRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class SaveAlertDistanceUseCaseTest {

    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var saveAlertDistanceUseCase: SaveAlertDistanceUseCase

    @BeforeEach
    fun setUp() {
        userPreferencesRepository = mockk()
        saveAlertDistanceUseCase = SaveAlertDistanceUseCase(userPreferencesRepository)
    }

    @Test
    fun `invoke saves distance successfully`() = runTest {
        // Given
        val distance = 1.5f
        coEvery { userPreferencesRepository.saveDistance(distance) } returns Result.success(Unit)

        // When
        val result = saveAlertDistanceUseCase(distance)

        // Then
        assertTrue(result.isSuccess)
        coVerify { userPreferencesRepository.saveDistance(distance) }
    }

    @Test
    fun `invoke handles save failure`() = runTest {
        // Given
        val distance = 2.0f
        val exception = RuntimeException("Save failed")
        coEvery { userPreferencesRepository.saveDistance(distance) } returns Result.failure(exception)

        // When
        val result = saveAlertDistanceUseCase(distance)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        coVerify { userPreferencesRepository.saveDistance(distance) }
    }

    @Test
    fun `invoke saves various distance values`() = runTest {
        // Given
        val distances = listOf(0.5f, 1.0f, 2.5f, 5.0f)
        
        for (distance in distances) {
            coEvery { userPreferencesRepository.saveDistance(distance) } returns Result.success(Unit)
            
            // When
            val result = saveAlertDistanceUseCase(distance)
            
            // Then
            assertTrue(result.isSuccess)
            coVerify { userPreferencesRepository.saveDistance(distance) }
        }
    }

    @Test
    fun `invoke saves minimum distance`() = runTest {
        // Given
        val minDistance = 0.1f
        coEvery { userPreferencesRepository.saveDistance(minDistance) } returns Result.success(Unit)

        // When
        val result = saveAlertDistanceUseCase(minDistance)

        // Then
        assertTrue(result.isSuccess)
        coVerify { userPreferencesRepository.saveDistance(minDistance) }
    }
}