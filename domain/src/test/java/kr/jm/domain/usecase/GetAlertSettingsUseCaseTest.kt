package kr.jm.domain.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kr.jm.domain.model.AlertSettings
import kr.jm.domain.repository.LocationRepository
import kotlin.test.assertEquals
import org.junit.Before
import org.junit.Test

class GetAlertSettingsUseCaseTest {

    private lateinit var locationRepository: LocationRepository
    private lateinit var getAlertSettingsUseCase: GetAlertSettingsUseCase

    @Before
    fun setUp() {
        locationRepository = mockk()
        getAlertSettingsUseCase = GetAlertSettingsUseCase(locationRepository)
    }

    @Test
    fun `invoke returns alert settings flow from repository`() = runTest {
        // Given
        val expectedSettings = AlertSettings(
            alertDistance = 1.5f,
            notiTitle = "Test Title",
            notiContent = "Test Content"
        )
        every { locationRepository.getAlertSettings() } returns flowOf(expectedSettings)

        // When
        val result = getAlertSettingsUseCase()

        // Then
        result.collect { settings ->
            assertEquals(expectedSettings, settings)
        }
        verify { locationRepository.getAlertSettings() }
    }

    @Test
    fun `invoke returns default alert settings`() = runTest {
        // Given
        val defaultSettings = AlertSettings(
            alertDistance = 0.5f,
            notiTitle = "하차 알림",
            notiContent = "곧 도착합니다"
        )
        every { locationRepository.getAlertSettings() } returns flowOf(defaultSettings)

        // When
        val result = getAlertSettingsUseCase()

        // Then
        result.collect { settings ->
            assertEquals(defaultSettings.alertDistance, settings.alertDistance)
            assertEquals(defaultSettings.notiTitle, settings.notiTitle)
            assertEquals(defaultSettings.notiContent, settings.notiContent)
        }
        verify { locationRepository.getAlertSettings() }
    }

    @Test
    fun `invoke returns updated alert settings`() = runTest {
        // Given
        val initialSettings = AlertSettings(1.0f, "Title 1", "Content 1")
        val updatedSettings = AlertSettings(2.0f, "Title 2", "Content 2")
        every { locationRepository.getAlertSettings() } returns flowOf(initialSettings, updatedSettings)

        // When
        val result = getAlertSettingsUseCase()

        // Then
        val collectedSettings = mutableListOf<AlertSettings>()
        result.collect { settings ->
            collectedSettings.add(settings)
        }
        assertEquals(2, collectedSettings.size)
        assertEquals(initialSettings, collectedSettings[0])
        assertEquals(updatedSettings, collectedSettings[1])
        verify { locationRepository.getAlertSettings() }
    }
}
