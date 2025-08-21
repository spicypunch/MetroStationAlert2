package kr.jm.feature_search

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kr.jm.domain.model.SubwayStation
import kr.jm.domain.usecase.*
import kr.jm.feature_search.util.MainDispatcherRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getSubwayStationsUseCase: GetSubwayStationsUseCase
    private lateinit var searchSubwayStationsUseCase: SearchSubwayStationsUseCase
    private lateinit var addBookmarkUseCase: AddBookmarkUseCase
    private lateinit var removeBookmarkUseCase: RemoveBookmarkUseCase
    private lateinit var addAlertStationUseCase: AddAlertStationUseCase
    private lateinit var getAddedAlertStationUseCase: GetAddedAlertStationUseCase

    private lateinit var viewModel: SearchViewModel
    private lateinit var fakeStations: List<SubwayStation>

    @Before
    fun setUp() {
        fakeStations = listOf(
            SubwayStation("1", "강남역", "2호선", 0.0, 0.0),
            SubwayStation("2", "역삼역", "2호선", 0.0, 0.0),
            SubwayStation("3", "야탑역", "분당선", 0.0, 0.0)
        )

        getSubwayStationsUseCase = mockk()
        searchSubwayStationsUseCase = mockk()
        addBookmarkUseCase = mockk()
        removeBookmarkUseCase = mockk()
        addAlertStationUseCase = mockk()
        getAddedAlertStationUseCase = mockk()

        every { getSubwayStationsUseCase() } returns flowOf(fakeStations)
        every { getAddedAlertStationUseCase() } returns flowOf("잠실역")
        
        // searchSubwayStationsUseCase에 대한 기본 동작 정의 추가
        coEvery { searchSubwayStationsUseCase(any(), any()) } returns emptyList()


        viewModel = SearchViewModel(
            getSubwayStationsUseCase,
            searchSubwayStationsUseCase,
            addBookmarkUseCase,
            removeBookmarkUseCase,
            addAlertStationUseCase,
            getAddedAlertStationUseCase
        )
    }

    @Test
    fun `onSearchQueryChanged 호출 시 uiState의 searchQuery가 변경되어야 한다`() {
        // Given
        val newQuery = "test query"

        // When
        viewModel.onSearchQueryChanged(newQuery)

        // Then
        val currentState = viewModel.uiState.value
        assertEquals(newQuery, currentState.searchQuery)
    }

    @Test
    fun `addBookmark 호출 시 addBookmarkUseCase가 호출되어야 한다`() = runTest {
        // Given
        val stationName = "강남역"
        coEvery { addBookmarkUseCase(stationName) } returns Result.success(stationName)

        // When
        viewModel.addBookmark(stationName)

        // Then
        coVerify(exactly = 1) { addBookmarkUseCase(stationName) }
    }

    @Test
    fun `검색어 입력 시 최종 검색 결과가 올바르게 반영되어야 한다`() = runTest {
        // Given
        val query = "야탑"
        val searchResult = listOf(SubwayStation("3", "야탑역", "분당선", 0.0, 0.0))
        val initialStations = viewModel.uiState.value.allStations
        coEvery { searchSubwayStationsUseCase(query, initialStations) } returns searchResult

        // When
        viewModel.onSearchQueryChanged(query)
        viewModel.searchStations()

        // Then
        // ViewModel에서 실행된 코루틴이 완료되도록 보장
        mainDispatcherRule.testDispatcher.scheduler.runCurrent()

        val finalState = viewModel.uiState.value
        assertFalse(finalState.isLoading, "로딩 상태가 false여야 합니다.")
        assertEquals(searchResult, finalState.filteredStations, "검색 결과가 일치해야 합니다.")
        assertEquals(query, finalState.searchQuery, "검색어가 일치해야 합니다.")
    }
}