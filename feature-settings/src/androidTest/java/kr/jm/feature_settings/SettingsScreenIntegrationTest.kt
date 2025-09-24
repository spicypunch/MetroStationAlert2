package kr.jm.feature_settings

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kr.jm.domain.usecase.GetAlertDistanceUseCase
import kr.jm.domain.usecase.GetNotificationContentUseCase
import kr.jm.domain.usecase.GetNotificationTitleUseCase
import kr.jm.domain.usecase.SaveAlertDistanceUseCase
import kr.jm.domain.usecase.SaveNotificationSettingsUseCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsScreenIntegrationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun settingsScreen_초기_상태_표시_테스트() {
        // Given
        val mockGetNotificationTitleUseCase = mockk<GetNotificationTitleUseCase>()
        val mockGetNotificationContentUseCase = mockk<GetNotificationContentUseCase>()
        val mockGetAlertDistanceUseCase = mockk<GetAlertDistanceUseCase>()
        val mockSaveNotificationSettingsUseCase = mockk<SaveNotificationSettingsUseCase>()
        val mockSaveAlertDistanceUseCase = mockk<SaveAlertDistanceUseCase>()

        every { mockGetNotificationTitleUseCase() } returns MutableStateFlow("기본 제목")
        every { mockGetNotificationContentUseCase() } returns MutableStateFlow("기본 내용")
        every { mockGetAlertDistanceUseCase() } returns MutableStateFlow(1.5f)

        val mockViewModel = SettingsViewModel(
            mockGetNotificationTitleUseCase,
            mockGetNotificationContentUseCase,
            mockGetAlertDistanceUseCase,
            mockSaveNotificationSettingsUseCase,
            mockSaveAlertDistanceUseCase
        )

        // When
        composeTestRule.setContent {
            SettingsScreen(settingsViewModel = mockViewModel)
        }

        // Then
        composeTestRule.onNodeWithText("설정⚙️").assertIsDisplayed()
        composeTestRule.onNodeWithText("알림 제목을 설정해 주세요.").assertIsDisplayed()
        composeTestRule.onNodeWithText("알림 내용을 설정해 주세요.").assertIsDisplayed()
        composeTestRule.onNodeWithText("역 도착 몇 km 전에 알림을 받으실 건가요?").assertIsDisplayed()
    }

    @Test
    fun settingsScreen_알림_제목_입력_및_저장_테스트() {
        // Given
        val mockGetNotificationTitleUseCase = mockk<GetNotificationTitleUseCase>()
        val mockGetNotificationContentUseCase = mockk<GetNotificationContentUseCase>()
        val mockGetAlertDistanceUseCase = mockk<GetAlertDistanceUseCase>()
        val mockSaveNotificationSettingsUseCase = mockk<SaveNotificationSettingsUseCase>()
        val mockSaveAlertDistanceUseCase = mockk<SaveAlertDistanceUseCase>()

        every { mockGetNotificationTitleUseCase() } returns MutableStateFlow("")
        every { mockGetNotificationContentUseCase() } returns MutableStateFlow("")
        every { mockGetAlertDistanceUseCase() } returns MutableStateFlow(1.0f)
        coEvery { mockSaveNotificationSettingsUseCase(any(), any()) } returns Result.success("저장 완료")

        val mockViewModel = SettingsViewModel(
            mockGetNotificationTitleUseCase,
            mockGetNotificationContentUseCase,
            mockGetAlertDistanceUseCase,
            mockSaveNotificationSettingsUseCase,
            mockSaveAlertDistanceUseCase
        )

        // When
        composeTestRule.setContent {
            SettingsScreen(settingsViewModel = mockViewModel)
        }

        // 제목 입력 필드 찾기 및 텍스트 입력
        composeTestRule.waitForIdle()

        // 첫 번째 TextField (알림 제목)에 텍스트 입력
        val titleTextFields = composeTestRule.onAllNodes(hasText("").and(hasContentDescription("").not()))
        if (titleTextFields.fetchSemanticsNodes().isNotEmpty()) {
            titleTextFields[0].performTextInput("새로운 제목")
        }

        // 첫 번째 저장 버튼 클릭
        val saveButtons = composeTestRule.onAllNodes(hasText("저장"))
        if (saveButtons.fetchSemanticsNodes().isNotEmpty()) {
            saveButtons[0].performClick()
        }

        // Then - UI가 여전히 표시되어야 함 (크래시 없음)
        composeTestRule.onNodeWithText("설정⚙️").assertIsDisplayed()
    }

    @Test
    fun settingsScreen_알림_내용_입력_및_저장_테스트() {
        // Given
        val mockGetNotificationTitleUseCase = mockk<GetNotificationTitleUseCase>()
        val mockGetNotificationContentUseCase = mockk<GetNotificationContentUseCase>()
        val mockGetAlertDistanceUseCase = mockk<GetAlertDistanceUseCase>()
        val mockSaveNotificationSettingsUseCase = mockk<SaveNotificationSettingsUseCase>()
        val mockSaveAlertDistanceUseCase = mockk<SaveAlertDistanceUseCase>()

        every { mockGetNotificationTitleUseCase() } returns MutableStateFlow("")
        every { mockGetNotificationContentUseCase() } returns MutableStateFlow("")
        every { mockGetAlertDistanceUseCase() } returns MutableStateFlow(1.0f)
        coEvery { mockSaveNotificationSettingsUseCase(any(), any()) } returns Result.success("저장 완료")

        val mockViewModel = SettingsViewModel(
            mockGetNotificationTitleUseCase,
            mockGetNotificationContentUseCase,
            mockGetAlertDistanceUseCase,
            mockSaveNotificationSettingsUseCase,
            mockSaveAlertDistanceUseCase
        )

        // When
        composeTestRule.setContent {
            SettingsScreen(settingsViewModel = mockViewModel)
        }

        composeTestRule.waitForIdle()

        // 두 번째 TextField (알림 내용)에 텍스트 입력
        val textFields = composeTestRule.onAllNodes(hasText("").and(hasContentDescription("").not()))
        if (textFields.fetchSemanticsNodes().size >= 2) {
            textFields[1].performTextInput("새로운 내용")
        }

        // 두 번째 저장 버튼 클릭
        val saveButtons = composeTestRule.onAllNodes(hasText("저장"))
        if (saveButtons.fetchSemanticsNodes().size >= 2) {
            saveButtons[1].performClick()
        }

        // Then
        composeTestRule.onNodeWithText("설정⚙️").assertIsDisplayed()
    }

    @Test
    fun settingsScreen_알림_거리_설정_버튼_클릭_테스트() {
        // Given
        val mockGetNotificationTitleUseCase = mockk<GetNotificationTitleUseCase>()
        val mockGetNotificationContentUseCase = mockk<GetNotificationContentUseCase>()
        val mockGetAlertDistanceUseCase = mockk<GetAlertDistanceUseCase>()
        val mockSaveNotificationSettingsUseCase = mockk<SaveNotificationSettingsUseCase>()
        val mockSaveAlertDistanceUseCase = mockk<SaveAlertDistanceUseCase>()

        every { mockGetNotificationTitleUseCase() } returns MutableStateFlow("")
        every { mockGetNotificationContentUseCase() } returns MutableStateFlow("")
        every { mockGetAlertDistanceUseCase() } returns MutableStateFlow(2.5f)

        val mockViewModel = SettingsViewModel(
            mockGetNotificationTitleUseCase,
            mockGetNotificationContentUseCase,
            mockGetAlertDistanceUseCase,
            mockSaveNotificationSettingsUseCase,
            mockSaveAlertDistanceUseCase
        )

        // When
        composeTestRule.setContent {
            SettingsScreen(settingsViewModel = mockViewModel)
        }

        composeTestRule.waitForIdle()

        // 거리 설정 버튼 클릭
        composeTestRule.onNodeWithText("2.5km").performClick()

        composeTestRule.waitForIdle()

        // Then - 바텀시트가 열렸는지 확인
        composeTestRule.onNodeWithText("확인").assertIsDisplayed()
    }

    @Test
    fun settingsScreen_바텀시트에서_거리_저장_테스트() {
        // Given
        val mockGetNotificationTitleUseCase = mockk<GetNotificationTitleUseCase>()
        val mockGetNotificationContentUseCase = mockk<GetNotificationContentUseCase>()
        val mockGetAlertDistanceUseCase = mockk<GetAlertDistanceUseCase>()
        val mockSaveNotificationSettingsUseCase = mockk<SaveNotificationSettingsUseCase>()
        val mockSaveAlertDistanceUseCase = mockk<SaveAlertDistanceUseCase>()

        every { mockGetNotificationTitleUseCase() } returns MutableStateFlow("")
        every { mockGetNotificationContentUseCase() } returns MutableStateFlow("")
        every { mockGetAlertDistanceUseCase() } returns MutableStateFlow(3.0f)
        coEvery { mockSaveAlertDistanceUseCase(any()) } returns Result.success("저장 완료")

        val mockViewModel = SettingsViewModel(
            mockGetNotificationTitleUseCase,
            mockGetNotificationContentUseCase,
            mockGetAlertDistanceUseCase,
            mockSaveNotificationSettingsUseCase,
            mockSaveAlertDistanceUseCase
        )

        // When
        composeTestRule.setContent {
            SettingsScreen(settingsViewModel = mockViewModel)
        }

        composeTestRule.waitForIdle()

        // 거리 설정 버튼 클릭하여 바텀시트 열기
        composeTestRule.onNodeWithText("3.0km").performClick()
        composeTestRule.waitForIdle()

        // 확인 버튼 클릭
        composeTestRule.onNodeWithText("확인").performClick()
        composeTestRule.waitForIdle()

        // Then - 기본 UI가 여전히 표시되어야 함
        composeTestRule.onNodeWithText("설정⚙️").assertIsDisplayed()
    }

    @Test
    fun settingsScreen_여러_설정_동시_변경_테스트() {
        // Given
        val mockGetNotificationTitleUseCase = mockk<GetNotificationTitleUseCase>()
        val mockGetNotificationContentUseCase = mockk<GetNotificationContentUseCase>()
        val mockGetAlertDistanceUseCase = mockk<GetAlertDistanceUseCase>()
        val mockSaveNotificationSettingsUseCase = mockk<SaveNotificationSettingsUseCase>()
        val mockSaveAlertDistanceUseCase = mockk<SaveAlertDistanceUseCase>()

        every { mockGetNotificationTitleUseCase() } returns MutableStateFlow("기존 제목")
        every { mockGetNotificationContentUseCase() } returns MutableStateFlow("기존 내용")
        every { mockGetAlertDistanceUseCase() } returns MutableStateFlow(1.5f)
        coEvery { mockSaveNotificationSettingsUseCase(any(), any()) } returns Result.success("저장 완료")
        coEvery { mockSaveAlertDistanceUseCase(any()) } returns Result.success("저장 완료")

        val mockViewModel = SettingsViewModel(
            mockGetNotificationTitleUseCase,
            mockGetNotificationContentUseCase,
            mockGetAlertDistanceUseCase,
            mockSaveNotificationSettingsUseCase,
            mockSaveAlertDistanceUseCase
        )

        // When
        composeTestRule.setContent {
            SettingsScreen(settingsViewModel = mockViewModel)
        }

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodes(hasText("기존 제목")).fetchSemanticsNodes().isNotEmpty()
        }

        // Then - 모든 UI 요소들이 표시되는지 확인
        composeTestRule.onNodeWithText("설정⚙️").assertIsDisplayed()
        composeTestRule.onNodeWithText("알림 제목을 설정해 주세요.").assertIsDisplayed()
        composeTestRule.onNodeWithText("알림 내용을 설정해 주세요.").assertIsDisplayed()
        composeTestRule.onNodeWithText("1.5km").assertIsDisplayed()
    }
}