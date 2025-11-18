package kr.jm.feature_search

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kr.jm.domain.model.SubwayStation
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchScreenIntegrationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun searchScreen_초기_상태_표시_테스트() {
        // Given
        val mockViewModel = mockk<SearchViewModel>(relaxed = true)
        val uiState = SearchScreenState()
        every { mockViewModel.uiState } returns MutableStateFlow(uiState)

        // When
        composeTestRule.setContent {
            SearchScreen(searchViewModel = mockViewModel)
        }

        // Then
        composeTestRule.onNodeWithText("하차 알리미🔔").assertIsDisplayed()
        composeTestRule.onNodeWithText("검색").assertIsDisplayed()
        composeTestRule.onNodeWithText("노선 필터").assertIsDisplayed()
    }

    @Test
    fun searchScreen_등록된_역_표시_테스트() {
        // Given
        val mockViewModel = mockk<SearchViewModel>(relaxed = true)
        val uiState = SearchScreenState(addedAlertStation = "강남역")
        every { mockViewModel.uiState } returns MutableStateFlow(uiState)

        // When
        composeTestRule.setContent {
            SearchScreen(searchViewModel = mockViewModel)
        }

        // Then
        composeTestRule.onNodeWithText("등록된 역").assertIsDisplayed()
        composeTestRule.onNodeWithText("강남역").assertIsDisplayed()
    }

    @Test
    fun searchScreen_검색_버튼_클릭_테스트() {
        // Given
        val mockViewModel = mockk<SearchViewModel>(relaxed = true)
        val uiState = SearchScreenState(searchQuery = "강남")
        every { mockViewModel.uiState } returns MutableStateFlow(uiState)
        every { mockViewModel.searchStations() } returns Unit

        composeTestRule.setContent {
            SearchScreen(searchViewModel = mockViewModel)
        }

        // When
        composeTestRule.onNodeWithText("검색").performClick()

        // Then
        verify(exactly = 1) { mockViewModel.searchStations() }
    }

    @Test
    fun searchScreen_알림_아이콘_클릭_테스트() {
        val mockViewModel = mockk<SearchViewModel>(relaxed = true)
        val station = SubwayStation("1", "강남역", "2호선", 0.0, 0.0)
        val uiState = SearchScreenState(
            filteredStations = listOf(station),
            allStations = listOf(station)
        )
        every { mockViewModel.uiState } returns MutableStateFlow(uiState)

        composeTestRule.setContent {
            SearchScreen(searchViewModel = mockViewModel)
        }

        composeTestRule.onNodeWithContentDescription("Notifications").performClick()

        verify { mockViewModel.addAlertStation("강남역") }
    }

    @Test
    fun searchScreen_북마크_아이콘_클릭_테스트() {
        val mockViewModel = mockk<SearchViewModel>(relaxed = true)
        val station = SubwayStation("1", "강남역", "2호선", 0.0, 0.0, isBookmark = false)
        val uiState = SearchScreenState(
            filteredStations = listOf(station),
            allStations = listOf(station)
        )
        every { mockViewModel.uiState } returns MutableStateFlow(uiState)

        composeTestRule.setContent {
            SearchScreen(searchViewModel = mockViewModel)
        }

        composeTestRule.onNodeWithContentDescription("Not bookmarked").performClick()

        verify { mockViewModel.addBookmark("강남역") }
    }
}
