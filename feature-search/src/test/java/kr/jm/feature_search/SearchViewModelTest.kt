package kr.jm.feature_search

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
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
    private lateinit var getAlertStateUseCase: GetAlertStateUseCase
    private lateinit var reactivateAlertUseCase: ReactivateAlertUseCase

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
        getAlertStateUseCase = mockk()
        reactivateAlertUseCase = mockk(relaxed = true)

        every { getSubwayStationsUseCase() } returns flowOf(fakeStations)
        every { getAddedAlertStationUseCase() } returns flowOf("잠실역")
        every { getAlertStateUseCase() } returns flowOf(true)
        
        // searchSubwayStationsUseCase에 대한 기본 동작 정의 추가
        coEvery { searchSubwayStationsUseCase(any(), any()) } returns emptyList()


        viewModel = SearchViewModel(
            getSubwayStationsUseCase,
            searchSubwayStationsUseCase,
            addBookmarkUseCase,
            removeBookmarkUseCase,
            addAlertStationUseCase,
            getAddedAlertStationUseCase,
            getAlertStateUseCase,
            reactivateAlertUseCase
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

    @Test
    fun `searchStations 호출 시 에러 발생하면 에러 상태가 반영되어야 한다`() = runTest {
        // Given
        val query = "테스트"
        val errorMessage = "Search failed"
        val exception = Exception(errorMessage)
        coEvery { searchSubwayStationsUseCase(any(), any()) } throws exception

        // When
        viewModel.onSearchQueryChanged(query)
        viewModel.searchStations()

        // Then
        mainDispatcherRule.testDispatcher.scheduler.runCurrent()

        val finalState = viewModel.uiState.value
        assertFalse(finalState.isLoading)
        assertEquals(errorMessage, finalState.errorMessage)
    }

    @Test
    fun `onDropdownToggle 호출 시 dropDownExpanded 상태가 토글되어야 한다`() {
        // Given
        val initialExpanded = viewModel.uiState.value.dropDownExpanded

        // When
        viewModel.onDropdownToggle()

        // Then
        val finalState = viewModel.uiState.value
        assertEquals(!initialExpanded, finalState.dropDownExpanded)
    }

    @Test
    fun `onSelectedLineChanged 호출 시 selectedLineName이 변경되어야 한다`() {
        // Given
        val newLineName = "1호선"

        // When
        viewModel.onSelectedLineChanged(newLineName)

        // Then
        val finalState = viewModel.uiState.value
        assertEquals(newLineName, finalState.selectedLineName)
    }

    @Test
    fun `filterStationName 호출 시 노선별 필터링이 올바르게 동작해야 한다`() = runTest {
        // Given
        val lineName = "2호선"
        
        // 초기 데이터가 로드될 때까지 기다림
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.filterStationName(lineName)

        // Then
        val finalState = viewModel.uiState.value
        val expectedStations = fakeStations.filter { it.lineName == lineName }
        assertEquals(expectedStations, finalState.filteredStations)
    }

    @Test
    fun `filterStationName에 전체 입력 시 모든 역이 표시되어야 한다`() = runTest {
        // Given
        val lineName = "전체"
        
        // 초기 데이터가 로드될 때까지 기다림
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.filterStationName(lineName)

        // Then
        val finalState = viewModel.uiState.value
        assertEquals(fakeStations, finalState.filteredStations)
    }

    @Test
    fun `addAlertStation 호출 시 addAlertStationUseCase가 호출되어야 한다`() = runTest {
        // Given
        val stationName = "강남역"
        val station = fakeStations.first { it.stationName == stationName }
        coEvery {
            addAlertStationUseCase(
                station.stationName,
                station.latitude,
                station.longitude
            )
        } returns Result.success(stationName)

        // When
        viewModel.addAlertStation(stationName)

        // Then
        coVerify(exactly = 1) {
            addAlertStationUseCase(
                station.stationName,
                station.latitude,
                station.longitude
            )
        }
    }

    @Test
    fun `removeBookmark 호출 시 removeBookmarkUseCase가 호출되어야 한다`() = runTest {
        // Given
        val stationName = "강남역"
        coEvery { removeBookmarkUseCase(stationName) } returns Result.success(stationName)

        // When
        viewModel.removeBookmark(stationName)

        // Then
        coVerify(exactly = 1) { removeBookmarkUseCase(stationName) }
    }

    @Test
    fun `초기 로딩 시 addedAlertStation이 올바르게 설정되어야 한다`() = runTest {
        // Given
        val expectedAlertStation = "잠실역"

        // When
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = viewModel.uiState.value
        assertEquals(expectedAlertStation, finalState.addedAlertStation)
    }

    @Test
    fun `uiState가 초기에 올바른 값으로 설정되어야 한다`() = runTest {
        // When - 충분한 시간 대기하여 초기화 완료 보장
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        
        // 추가로 조금 더 기다려서 collectLatest 완료 보장
        mainDispatcherRule.testDispatcher.scheduler.runCurrent()

        // Then
        val state = viewModel.uiState.value
        assertEquals(fakeStations, state.allStations)
        assertEquals(fakeStations, state.filteredStations) // 초기에는 전체 표시
        assertFalse(state.isLoading)
        assertEquals("", state.searchQuery)
        assertEquals("전체", state.selectedLineName)
        assertFalse(state.dropDownExpanded)
        assertEquals("잠실역", state.addedAlertStation) // getAddedAlertStationUseCase에서 설정된 값
        assertTrue(state.isAlertActive)
    }

    @Test
    fun `getSubwayStationsUseCase에서 에러 발생 시 에러 상태가 반영되어야 한다`() = runTest {
        // Given
        val errorMessage = "Failed to load stations"
        val errorUseCase: GetSubwayStationsUseCase = mockk()
        every { errorUseCase() } returns flow<List<SubwayStation>> { 
            throw Exception(errorMessage)
        }

        // When
        val errorViewModel = SearchViewModel(
            errorUseCase,
            searchSubwayStationsUseCase,
            addBookmarkUseCase,
            removeBookmarkUseCase,
            addAlertStationUseCase,
            getAddedAlertStationUseCase,
            getAlertStateUseCase,
            reactivateAlertUseCase
        )

        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = errorViewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Failed to load stations", state.errorMessage)
    }

    @Test
    fun `uiState Flow가 올바르게 상태 변화를 방출해야 한다`() = runTest {
        // Given & When & Then
        viewModel.uiState.test {
            val initialState = awaitItem()
            
            // 검색어 변경
            viewModel.onSearchQueryChanged("강남")
            val searchQueryChanged = awaitItem()
            assertEquals("강남", searchQueryChanged.searchQuery)
            
            // 드롭다운 토글
            viewModel.onDropdownToggle()
            val dropdownToggled = awaitItem()
            assertTrue(dropdownToggled.dropDownExpanded)
            
            // 노선 선택 변경
            viewModel.onSelectedLineChanged("1호선")
            val lineChanged = awaitItem()
            assertEquals("1호선", lineChanged.selectedLineName)
        }
    }

    @Test
    fun `addBookmark 에러 처리 테스트`() = runTest {
        // Given
        val stationName = "강남역"
        val errorMessage = "북마크 추가 실패"
        coEvery { addBookmarkUseCase(stationName) } returns Result.failure(Exception(errorMessage))

        // When
        viewModel.addBookmark(stationName)

        // Then
        coVerify(exactly = 1) { addBookmarkUseCase(stationName) }
        // 실제 ViewModel에서 에러 처리가 구현되어 있지 않으므로 
        // 향후 에러 처리 로직 추가 시 테스트 확장 가능
    }

    @Test
    fun `reactivateAlert 호출 시 usecase가 실행되어야 한다`() = runTest {
        // When
        viewModel.reactivateAlert()

        // Then
        coVerify(exactly = 1) { reactivateAlertUseCase() }
    }

    @Test
    fun `동시에 여러 검색이 실행될 때 최신 결과만 반영되어야 한다`() = runTest {
        // Given
        val query1 = "강남"
        val query2 = "역삼"
        val result1 = listOf(SubwayStation("1", "강남역", "2호선", 0.0, 0.0))
        val result2 = listOf(SubwayStation("2", "역삼역", "2호선", 0.0, 0.0))
        
        coEvery { searchSubwayStationsUseCase(query1, any()) } returns result1
        coEvery { searchSubwayStationsUseCase(query2, any()) } returns result2

        // When
        viewModel.onSearchQueryChanged(query1)
        viewModel.searchStations()
        viewModel.onSearchQueryChanged(query2)
        viewModel.searchStations()

        // Then
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertEquals(query2, finalState.searchQuery)
        assertEquals(result2, finalState.filteredStations)
    }
}
