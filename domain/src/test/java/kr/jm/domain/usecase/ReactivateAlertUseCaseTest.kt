package kr.jm.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kr.jm.domain.repository.LocationRepository
import kotlin.test.assertEquals
import org.junit.Before
import org.junit.Test

class ReactivateAlertUseCaseTest {

    private lateinit var locationRepository: LocationRepository
    private lateinit var reactivateAlertUseCase: ReactivateAlertUseCase

    @Before
    fun setUp() {
        locationRepository = mockk()
        reactivateAlertUseCase = ReactivateAlertUseCase(locationRepository)
    }

    @Test
    fun `invoke calls reactivateAlert on repository`() = runTest {
        // Given
        coEvery { locationRepository.reactivateAlert() } returns Unit

        // When
        reactivateAlertUseCase()

        // Then
        coVerify { locationRepository.reactivateAlert() }
    }

    @Test
    fun `invoke handles repository exceptions`() = runTest {
        // Given
        val exception = RuntimeException("Test exception")
        coEvery { locationRepository.reactivateAlert() } throws exception

        // When & Then
        try {
            reactivateAlertUseCase()
        } catch (e: Exception) {
            assertEquals("Test exception", e.message)
        }
        coVerify { locationRepository.reactivateAlert() }
    }

    @Test
    fun `invoke is suspend function`() = runTest {
        // Given
        coEvery { locationRepository.reactivateAlert() } returns Unit

        // When
        reactivateAlertUseCase.invoke()

        // Then
        coVerify { locationRepository.reactivateAlert() }
    }
}
