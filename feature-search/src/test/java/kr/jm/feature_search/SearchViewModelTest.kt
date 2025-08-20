package kr.jm.feature_search

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

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    // 1. 모든 ViewModel 테스트에 재사용될 테스트 규칙을 적용합니다.
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // 2. 테스트 대상과 그 의존성들을 모두 선언합니다.
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

        // 3. 모든 의존성을 가짜(Mock) 객체로 초기화합니다.
        getSubwayStationsUseCase = mockk()
        searchSubwayStationsUseCase = mockk()
        addBookmarkUseCase = mockk()
        removeBookmarkUseCase = mockk()
        addAlertStationUseCase = mockk()
        getAddedAlertStationUseCase = mockk()

        // 4. ViewModel의 init {} 블록이 실행될 때 에러가 나지 않도록 기본 반환값을 설정해줍니다.
        every { getSubwayStationsUseCase() } returns flowOf(fakeStations)
        every { getAddedAlertStationUseCase() } returns flowOf("잠실역")

        // 5. 가짜 의존성들을 주입하여 ViewModel을 생성합니다.
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
        // UseCase가 성공적으로 끝났다고 가정
        coEvery { addBookmarkUseCase(stationName) } returns Result.success(stationName)

        // When
        viewModel.addBookmark(stationName)

        // Then
        // ViewModel이 UseCase를 올바르게 호출했는지 검증
        coVerify(exactly = 1) { addBookmarkUseCase(stationName) }
    }
}
