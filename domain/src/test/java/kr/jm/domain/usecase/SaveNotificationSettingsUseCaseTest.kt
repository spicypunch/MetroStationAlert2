package kr.jm.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kr.jm.domain.repository.UserPreferencesRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class SaveNotificationSettingsUseCaseTest {

    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var saveNotificationSettingsUseCase: SaveNotificationSettingsUseCase

    @BeforeEach
    fun setUp() {
        userPreferencesRepository = mockk()
        saveNotificationSettingsUseCase = SaveNotificationSettingsUseCase(userPreferencesRepository)
    }

    @Test
    fun `invoke saves notification settings successfully`() = runTest {
        // Given
        val title = "하차 알림"
        val content = "곧 도착합니다"
        coEvery { userPreferencesRepository.saveNotificationSettings(title, content) } returns Result.success(Unit)

        // When
        val result = saveNotificationSettingsUseCase(title, content)

        // Then
        assertTrue(result.isSuccess)
        coVerify { userPreferencesRepository.saveNotificationSettings(title, content) }
    }

    @Test
    fun `invoke handles save failure`() = runTest {
        // Given
        val title = "알림 제목"
        val content = "알림 내용"
        val exception = RuntimeException("Save failed")
        coEvery { userPreferencesRepository.saveNotificationSettings(title, content) } returns Result.failure(exception)

        // When
        val result = saveNotificationSettingsUseCase(title, content)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        coVerify { userPreferencesRepository.saveNotificationSettings(title, content) }
    }

    @Test
    fun `invoke saves empty strings`() = runTest {
        // Given
        val title = ""
        val content = ""
        coEvery { userPreferencesRepository.saveNotificationSettings(title, content) } returns Result.success(Unit)

        // When
        val result = saveNotificationSettingsUseCase(title, content)

        // Then
        assertTrue(result.isSuccess)
        coVerify { userPreferencesRepository.saveNotificationSettings(title, content) }
    }

    @Test
    fun `invoke saves various notification settings`() = runTest {
        // Given
        val settings = listOf(
            Pair("지하철 알림", "하차 준비해주세요"),
            Pair("도착 알림", "목적지에 도착했습니다"),
            Pair("긴급 알림", "지금 내리세요!")
        )
        
        for ((title, content) in settings) {
            coEvery { userPreferencesRepository.saveNotificationSettings(title, content) } returns Result.success(Unit)
            
            // When
            val result = saveNotificationSettingsUseCase(title, content)
            
            // Then
            assertTrue(result.isSuccess)
            coVerify { userPreferencesRepository.saveNotificationSettings(title, content) }
        }
    }

    @Test
    fun `invoke saves long notification content`() = runTest {
        // Given
        val title = "상세 알림"
        val content = "이번 역은 강남역입니다. 내리실 도어는 오른쪽입니다. 하차하실 분들은 미리 준비해주세요."
        coEvery { userPreferencesRepository.saveNotificationSettings(title, content) } returns Result.success(Unit)

        // When
        val result = saveNotificationSettingsUseCase(title, content)

        // Then
        assertTrue(result.isSuccess)
        coVerify { userPreferencesRepository.saveNotificationSettings(title, content) }
    }
}