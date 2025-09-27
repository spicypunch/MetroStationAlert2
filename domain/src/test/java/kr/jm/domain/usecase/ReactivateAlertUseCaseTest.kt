package kr.jm.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kr.jm.domain.repository.LocationRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ReactivateAlertUseCaseTest {

    private lateinit var locationRepository: LocationRepository
    private lateinit var reactivateAlertUseCase: ReactivateAlertUseCase

    @BeforeEach
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
            assert(e.message == "Test exception")
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