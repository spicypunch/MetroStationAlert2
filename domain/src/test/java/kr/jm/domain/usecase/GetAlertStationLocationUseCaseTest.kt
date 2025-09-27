package kr.jm.domain.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kr.jm.domain.model.LocationData
import kr.jm.domain.repository.LocationRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class GetAlertStationLocationUseCaseTest {

    private lateinit var locationRepository: LocationRepository
    private lateinit var getAlertStationLocationUseCase: GetAlertStationLocationUseCase

    @BeforeEach
    fun setUp() {
        locationRepository = mockk()
        getAlertStationLocationUseCase = GetAlertStationLocationUseCase(locationRepository)
    }

    @Test
    fun `invoke returns location data flow from repository`() = runTest {
        // Given
        val expectedLocation = LocationData(
            latitude = 37.5665,
            longitude = 126.9780
        )
        every { locationRepository.getAlertStationLocation() } returns flowOf(expectedLocation)

        // When
        val result = getAlertStationLocationUseCase()

        // Then
        result.collect { location ->
            assertEquals(expectedLocation, location)
        }
        verify { locationRepository.getAlertStationLocation() }
    }

    @Test
    fun `invoke returns default location data`() = runTest {
        // Given
        val defaultLocation = LocationData(
            latitude = 0.0,
            longitude = 0.0
        )
        every { locationRepository.getAlertStationLocation() } returns flowOf(defaultLocation)

        // When
        val result = getAlertStationLocationUseCase()

        // Then
        result.collect { location ->
            assertEquals(defaultLocation.latitude, location.latitude, 0.0)
            assertEquals(defaultLocation.longitude, location.longitude, 0.0)
        }
        verify { locationRepository.getAlertStationLocation() }
    }

    @Test
    fun `invoke returns updated location data`() = runTest {
        // Given
        val location1 = LocationData(37.5665, 126.9780)
        val location2 = LocationData(37.5640, 126.9769)
        every { locationRepository.getAlertStationLocation() } returns flowOf(location1, location2)

        // When
        val result = getAlertStationLocationUseCase()

        // Then
        val collectedLocations = mutableListOf<LocationData>()
        result.collect { location ->
            collectedLocations.add(location)
        }
        assertEquals(2, collectedLocations.size)
        assertEquals(location1, collectedLocations[0])
        assertEquals(location2, collectedLocations[1])
        verify { locationRepository.getAlertStationLocation() }
    }

    @Test
    fun `invoke returns specific subway station coordinates`() = runTest {
        // Given - 강남역 좌표 예시
        val gangnamLocation = LocationData(
            latitude = 37.4980,
            longitude = 127.0276
        )
        every { locationRepository.getAlertStationLocation() } returns flowOf(gangnamLocation)

        // When
        val result = getAlertStationLocationUseCase()

        // Then
        result.collect { location ->
            assertEquals(37.4980, location.latitude, 0.0001)
            assertEquals(127.0276, location.longitude, 0.0001)
        }
        verify { locationRepository.getAlertStationLocation() }
    }
}