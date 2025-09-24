package kr.jm.feature_settings

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kr.jm.domain.usecase.GetAlertDistanceUseCase
import kr.jm.domain.usecase.GetNotificationContentUseCase
import kr.jm.domain.usecase.GetNotificationTitleUseCase
import kr.jm.domain.usecase.SaveAlertDistanceUseCase
import kr.jm.domain.usecase.SaveNotificationSettingsUseCase
import kr.jm.feature_settings.util.MainDispatcherRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getNotificationTitleUseCase: GetNotificationTitleUseCase
    private lateinit var getNotificationContentUseCase: GetNotificationContentUseCase
    private lateinit var getAlertDistanceUseCase: GetAlertDistanceUseCase
    private lateinit var saveNotificationSettingsUseCase: SaveNotificationSettingsUseCase
    private lateinit var saveAlertDistanceUseCase: SaveAlertDistanceUseCase
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        getNotificationTitleUseCase = mockk()
        getNotificationContentUseCase = mockk()
        getAlertDistanceUseCase = mockk()
        saveNotificationSettingsUseCase = mockk()
        saveAlertDistanceUseCase = mockk()

        // 기본 mock 설정
        every { getNotificationTitleUseCase() } returns flowOf("기본 제목")
        every { getNotificationContentUseCase() } returns flowOf("기본 내용")
        every { getAlertDistanceUseCase() } returns flowOf(1.0f)
    }

    @Test
    fun `초기 설정 로딩이 올바르게 되어야 한다`() = runTest {
        // Given
        val expectedTitle = "테스트 제목"
        val expectedContent = "테스트 내용"
        val expectedDistance = 2.5f

        every { getNotificationTitleUseCase() } returns flowOf(expectedTitle)
        every { getNotificationContentUseCase() } returns flowOf(expectedContent)
        every { getAlertDistanceUseCase() } returns flowOf(expectedDistance)

        // When
        viewModel = SettingsViewModel(
            getNotificationTitleUseCase,
            getNotificationContentUseCase,
            getAlertDistanceUseCase,
            saveNotificationSettingsUseCase,
            saveAlertDistanceUseCase
        )
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertEquals(expectedTitle, state.notificationTitle)
        assertEquals(expectedContent, state.notificationContent)
        assertEquals(expectedDistance, state.alertDistance)
        assertFalse(state.isLoading)
        assertFalse(state.showBottomSheet)
        assertNull(state.errorMessage)
    }

    @Test
    fun `알림 제목 변경이 올바르게 동작해야 한다`() = runTest {
        // Given
        viewModel = SettingsViewModel(
            getNotificationTitleUseCase,
            getNotificationContentUseCase,
            getAlertDistanceUseCase,
            saveNotificationSettingsUseCase,
            saveAlertDistanceUseCase
        )
        val newTitle = "새로운 제목"

        // When
        viewModel.onNotificationTitleChanged(newTitle)

        // Then
        assertEquals(newTitle, viewModel.uiState.value.notificationTitle)
    }

    @Test
    fun `알림 내용 변경이 올바르게 동작해야 한다`() = runTest {
        // Given
        viewModel = SettingsViewModel(
            getNotificationTitleUseCase,
            getNotificationContentUseCase,
            getAlertDistanceUseCase,
            saveNotificationSettingsUseCase,
            saveAlertDistanceUseCase
        )
        val newContent = "새로운 내용"

        // When
        viewModel.onNotificationContentChanged(newContent)

        // Then
        assertEquals(newContent, viewModel.uiState.value.notificationContent)
    }

    @Test
    fun `알림 거리 변경이 올바르게 동작해야 한다`() = runTest {
        // Given
        viewModel = SettingsViewModel(
            getNotificationTitleUseCase,
            getNotificationContentUseCase,
            getAlertDistanceUseCase,
            saveNotificationSettingsUseCase,
            saveAlertDistanceUseCase
        )
        val newDistance = 3.0f

        // When
        viewModel.onAlertDistanceChanged(newDistance)

        // Then
        assertEquals(newDistance, viewModel.uiState.value.alertDistance)
    }

    @Test
    fun `바텀시트 표시 상태 변경이 올바르게 동작해야 한다`() = runTest {
        // Given
        viewModel = SettingsViewModel(
            getNotificationTitleUseCase,
            getNotificationContentUseCase,
            getAlertDistanceUseCase,
            saveNotificationSettingsUseCase,
            saveAlertDistanceUseCase
        )

        // When & Then - 바텀시트 보이기
        viewModel.onShowBottomSheet()
        assertTrue(viewModel.uiState.value.showBottomSheet)

        // When & Then - 바텀시트 숨기기
        viewModel.onHideBottomSheet()
        assertFalse(viewModel.uiState.value.showBottomSheet)
    }

    @Test
    fun `알림 제목 저장이 성공적으로 동작해야 한다`() = runTest {
        // Given
        viewModel = SettingsViewModel(
            getNotificationTitleUseCase,
            getNotificationContentUseCase,
            getAlertDistanceUseCase,
            saveNotificationSettingsUseCase,
            saveAlertDistanceUseCase
        )
        coEvery { saveNotificationSettingsUseCase(any(), any()) } returns Result.success("저장 완료")

        // When
        viewModel.saveNotificationTitle()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { saveNotificationSettingsUseCase(any(), any()) }
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `알림 제목 저장 실패 시 에러 메시지가 설정되어야 한다`() = runTest {
        // Given
        val errorMessage = "저장 실패"
        viewModel = SettingsViewModel(
            getNotificationTitleUseCase,
            getNotificationContentUseCase,
            getAlertDistanceUseCase,
            saveNotificationSettingsUseCase,
            saveAlertDistanceUseCase
        )
        coEvery { saveNotificationSettingsUseCase(any(), any()) } returns Result.failure(Exception(errorMessage))

        // When
        viewModel.saveNotificationTitle()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(errorMessage, viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `알림 내용 저장이 성공적으로 동작해야 한다`() = runTest {
        // Given
        viewModel = SettingsViewModel(
            getNotificationTitleUseCase,
            getNotificationContentUseCase,
            getAlertDistanceUseCase,
            saveNotificationSettingsUseCase,
            saveAlertDistanceUseCase
        )
        coEvery { saveNotificationSettingsUseCase(any(), any()) } returns Result.success("저장 완료")

        // When
        viewModel.saveNotificationContent()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { saveNotificationSettingsUseCase(any(), any()) }
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `알림 거리 저장이 성공적으로 동작해야 한다`() = runTest {
        // Given
        viewModel = SettingsViewModel(
            getNotificationTitleUseCase,
            getNotificationContentUseCase,
            getAlertDistanceUseCase,
            saveNotificationSettingsUseCase,
            saveAlertDistanceUseCase
        )
        coEvery { saveAlertDistanceUseCase(any()) } returns Result.success("저장 완료")

        // When
        viewModel.onShowBottomSheet() // 먼저 바텀시트를 보이게 함
        viewModel.saveAlertDistance()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { saveAlertDistanceUseCase(any()) }
        assertFalse(viewModel.uiState.value.showBottomSheet) // 저장 성공 시 바텀시트 숨김
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `알림 거리 저장 실패 시 에러 메시지가 설정되어야 한다`() = runTest {
        // Given
        val errorMessage = "거리 저장 실패"
        viewModel = SettingsViewModel(
            getNotificationTitleUseCase,
            getNotificationContentUseCase,
            getAlertDistanceUseCase,
            saveNotificationSettingsUseCase,
            saveAlertDistanceUseCase
        )
        coEvery { saveAlertDistanceUseCase(any()) } returns Result.failure(Exception(errorMessage))

        // When
        viewModel.saveAlertDistance()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(errorMessage, viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `에러 메시지 클리어가 올바르게 동작해야 한다`() = runTest {
        // Given
        viewModel = SettingsViewModel(
            getNotificationTitleUseCase,
            getNotificationContentUseCase,
            getAlertDistanceUseCase,
            saveNotificationSettingsUseCase,
            saveAlertDistanceUseCase
        )
        coEvery { saveAlertDistanceUseCase(any()) } returns Result.failure(Exception("에러"))

        // 먼저 에러를 발생시킴
        viewModel.saveAlertDistance()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.clearErrorMessage()

        // Then
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `여러 설정을 연속으로 변경했을 때 상태가 올바르게 유지되어야 한다`() = runTest {
        // Given
        viewModel = SettingsViewModel(
            getNotificationTitleUseCase,
            getNotificationContentUseCase,
            getAlertDistanceUseCase,
            saveNotificationSettingsUseCase,
            saveAlertDistanceUseCase
        )

        // When
        viewModel.onNotificationTitleChanged("새 제목")
        viewModel.onNotificationContentChanged("새 내용")
        viewModel.onAlertDistanceChanged(4.5f)
        viewModel.onShowBottomSheet()

        // Then
        val state = viewModel.uiState.value
        assertEquals("새 제목", state.notificationTitle)
        assertEquals("새 내용", state.notificationContent)
        assertEquals(4.5f, state.alertDistance)
        assertTrue(state.showBottomSheet)
    }
}