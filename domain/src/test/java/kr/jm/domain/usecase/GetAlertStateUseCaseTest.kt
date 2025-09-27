package kr.jm.domain.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kr.jm.domain.repository.LocationRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class GetAlertStateUseCaseTest {

    private lateinit var locationRepository: LocationRepository
    private lateinit var getAlertStateUseCase: GetAlertStateUseCase

    @BeforeEach
    fun setUp() {
        locationRepository = mockk()
        getAlertStateUseCase = GetAlertStateUseCase(locationRepository)
    }

    @Test
    fun `invoke returns alert state flow from repository`() = runTest {
        // Given
        val alertState = true
        every { locationRepository.getAlertState() } returns flowOf(alertState)

        // When
        val result = getAlertStateUseCase()

        // Then
        result.collect { state ->
            assertEquals(alertState, state)
        }
        verify { locationRepository.getAlertState() }
    }

    @Test
    fun `invoke returns false when alert is inactive`() = runTest {
        // Given
        val alertState = false
        every { locationRepository.getAlertState() } returns flowOf(alertState)

        // When
        val result = getAlertStateUseCase()

        // Then
        result.collect { state ->
            assertEquals(alertState, state)
        }
        verify { locationRepository.getAlertState() }
    }

    @Test
    fun `invoke returns multiple state changes`() = runTest {
        // Given
        val stateChanges = listOf(true, false, true)
        every { locationRepository.getAlertState() } returns flowOf(*stateChanges.toBooleanArray())

        // When
        val result = getAlertStateUseCase()

        // Then
        val collectedStates = mutableListOf<Boolean>()
        result.collect { state ->
            collectedStates.add(state)
        }
        assertEquals(stateChanges, collectedStates)
        verify { locationRepository.getAlertState() }
    }
}