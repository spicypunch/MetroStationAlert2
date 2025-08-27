package kr.jm.feature_search

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kr.jm.domain.model.SubwayStation
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SearchScreenTest {

    @Test
    fun `SearchScreenState 초기값이 올바르게 설정되어야 한다`() {
        // Given & When
        val state = SearchScreenState()

        // Then
        assertEquals("", state.searchQuery)
        assertEquals("", state.addedAlertStation)
        assertEquals(emptyList(), state.filteredStations)
        assertEquals("전체", state.selectedLineName)
        assertEquals(false, state.dropDownExpanded)
        assertEquals(false, state.isLoading)
        assertEquals(null, state.errorMessage)
        assertNotNull(state.subwayLineList)
    }

    @Test
    fun `SearchScreenState 커스텀 값으로 생성되어야 한다`() {
        // Given
        val stations = listOf(
            SubwayStation("", "강남역", "2호선", 37.498, 127.028, false)
        )

        // When
        val state = SearchScreenState(
            searchQuery = "강남",
            addedAlertStation = "강남역",
            filteredStations = stations,
            selectedLineName = "2호선",
            dropDownExpanded = true,
            isLoading = true,
            errorMessage = "에러 발생"
        )

        // Then
        assertEquals("강남", state.searchQuery)
        assertEquals("강남역", state.addedAlertStation)
        assertEquals(stations, state.filteredStations)
        assertEquals("2호선", state.selectedLineName)
        assertEquals(true, state.dropDownExpanded)
        assertEquals(true, state.isLoading)
        assertEquals("에러 발생", state.errorMessage)
    }

    @Test
    fun `ViewModel Mock이 올바르게 생성되어야 한다`() {
        // Given
        val mockViewModel = mockk<SearchViewModel>(relaxed = true)
        val uiState = SearchScreenState(searchQuery = "테스트")
        
        // When
        every { mockViewModel.uiState } returns MutableStateFlow(uiState)

        // Then
        assertEquals("테스트", mockViewModel.uiState.value.searchQuery)
    }
}