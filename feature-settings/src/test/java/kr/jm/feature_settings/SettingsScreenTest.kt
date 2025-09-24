package kr.jm.feature_settings

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SettingsScreenTest {

    @Test
    fun `SettingsScreenState 초기값이 올바르게 설정되어야 한다`() {
        // Given & When
        val state = SettingsScreenState()

        // Then
        assertEquals("", state.notificationTitle)
        assertEquals("", state.notificationContent)
        assertEquals(1.0f, state.alertDistance)
        assertTrue(state.isLoading)
        assertFalse(state.showBottomSheet)
        assertNull(state.errorMessage)
    }

    @Test
    fun `SettingsScreenState 커스텀 값으로 생성되어야 한다`() {
        // Given
        val title = "테스트 제목"
        val content = "테스트 내용"
        val distance = 2.5f
        val isLoading = false
        val showBottomSheet = true
        val errorMessage = "에러 메시지"

        // When
        val state = SettingsScreenState(
            notificationTitle = title,
            notificationContent = content,
            alertDistance = distance,
            isLoading = isLoading,
            showBottomSheet = showBottomSheet,
            errorMessage = errorMessage
        )

        // Then
        assertEquals(title, state.notificationTitle)
        assertEquals(content, state.notificationContent)
        assertEquals(distance, state.alertDistance)
        assertEquals(isLoading, state.isLoading)
        assertEquals(showBottomSheet, state.showBottomSheet)
        assertEquals(errorMessage, state.errorMessage)
    }

    @Test
    fun `SettingsScreenState copy가 올바르게 동작해야 한다`() {
        // Given
        val originalState = SettingsScreenState(
            notificationTitle = "원래 제목",
            notificationContent = "원래 내용",
            alertDistance = 1.5f,
            isLoading = true,
            showBottomSheet = false,
            errorMessage = null
        )

        // When
        val modifiedState = originalState.copy(
            notificationTitle = "새 제목",
            showBottomSheet = true
        )

        // Then
        assertEquals("새 제목", modifiedState.notificationTitle)
        assertEquals("원래 내용", modifiedState.notificationContent) // 변경되지 않은 값 유지
        assertEquals(1.5f, modifiedState.alertDistance) // 변경되지 않은 값 유지
        assertTrue(modifiedState.isLoading) // 변경되지 않은 값 유지
        assertTrue(modifiedState.showBottomSheet) // 변경된 값
        assertNull(modifiedState.errorMessage) // 변경되지 않은 값 유지
    }

    @Test
    fun `알림 거리의 범위가 적절한지 확인해야 한다`() {
        // Given & When
        val minDistanceState = SettingsScreenState(alertDistance = 0.5f)
        val maxDistanceState = SettingsScreenState(alertDistance = 5.0f)
        val midDistanceState = SettingsScreenState(alertDistance = 2.75f)

        // Then
        assertEquals(0.5f, minDistanceState.alertDistance)
        assertEquals(5.0f, maxDistanceState.alertDistance)
        assertEquals(2.75f, midDistanceState.alertDistance)
    }

    @Test
    fun `빈 문자열과 null 에러 메시지 처리를 확인해야 한다`() {
        // Given & When
        val emptyTitleState = SettingsScreenState(notificationTitle = "")
        val emptyContentState = SettingsScreenState(notificationContent = "")
        val nullErrorState = SettingsScreenState(errorMessage = null)
        val emptyErrorState = SettingsScreenState(errorMessage = "")

        // Then
        assertEquals("", emptyTitleState.notificationTitle)
        assertEquals("", emptyContentState.notificationContent)
        assertNull(nullErrorState.errorMessage)
        assertEquals("", emptyErrorState.errorMessage)
    }

    @Test
    fun `로딩 상태와 바텀시트 상태의 다양한 조합을 확인해야 한다`() {
        // Given & When
        val bothFalse = SettingsScreenState(isLoading = false, showBottomSheet = false)
        val bothTrue = SettingsScreenState(isLoading = true, showBottomSheet = true)
        val loadingOnlyTrue = SettingsScreenState(isLoading = true, showBottomSheet = false)
        val bottomSheetOnlyTrue = SettingsScreenState(isLoading = false, showBottomSheet = true)

        // Then
        assertFalse(bothFalse.isLoading && bothFalse.showBottomSheet)
        assertTrue(bothTrue.isLoading && bothTrue.showBottomSheet)
        assertTrue(loadingOnlyTrue.isLoading && !loadingOnlyTrue.showBottomSheet)
        assertTrue(!bottomSheetOnlyTrue.isLoading && bottomSheetOnlyTrue.showBottomSheet)
    }

    @Test
    fun `데이터 클래스 equals 동작을 확인해야 한다`() {
        // Given
        val state1 = SettingsScreenState(
            notificationTitle = "제목",
            notificationContent = "내용",
            alertDistance = 1.5f
        )
        val state2 = SettingsScreenState(
            notificationTitle = "제목",
            notificationContent = "내용",
            alertDistance = 1.5f
        )
        val state3 = SettingsScreenState(
            notificationTitle = "다른 제목",
            notificationContent = "내용",
            alertDistance = 1.5f
        )

        // When & Then
        assertEquals(state1, state2) // 같은 값들
        assertTrue(state1 != state3) // 다른 값들
        assertEquals(state1.hashCode(), state2.hashCode()) // 같은 해시코드
    }

    @Test
    fun `toString 메서드가 올바른 형태로 출력되는지 확인해야 한다`() {
        // Given
        val state = SettingsScreenState(
            notificationTitle = "테스트",
            notificationContent = "내용",
            alertDistance = 2.0f,
            isLoading = false,
            showBottomSheet = true,
            errorMessage = "에러"
        )

        // When
        val stringRepresentation = state.toString()

        // Then
        assertTrue(stringRepresentation.contains("SettingsScreenState"))
        assertTrue(stringRepresentation.contains("notificationTitle=테스트"))
        assertTrue(stringRepresentation.contains("notificationContent=내용"))
        assertTrue(stringRepresentation.contains("alertDistance=2.0"))
        assertTrue(stringRepresentation.contains("isLoading=false"))
        assertTrue(stringRepresentation.contains("showBottomSheet=true"))
        assertTrue(stringRepresentation.contains("errorMessage=에러"))
    }
}