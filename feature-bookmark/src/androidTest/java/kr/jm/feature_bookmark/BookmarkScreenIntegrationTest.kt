package kr.jm.feature_bookmark

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodes
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kr.jm.domain.model.SubwayArrivalResponse
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookmarkScreenIntegrationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun bookmarkScreen_빈_북마크_상태_표시_테스트() {
        val mockViewModel = mockk<BookmarkViewModel>(relaxed = true)
        every { mockViewModel.uiState } returns MutableStateFlow(BookmarkScreenState())

        composeTestRule.setContent {
            BookmarkScreen(bookmarkViewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("지하철 도착 정보🚊").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("새로고침").assertIsDisplayed()
    }

    @Test
    fun bookmarkScreen_북마크된_역_표시_테스트() {
        val mockViewModel = mockk<BookmarkViewModel>(relaxed = true)
        val response = createSampleResponse("강남역")
        val state = BookmarkScreenState(
            bookmarks = setOf("강남역"),
            arrivalTimeMap = mapOf("강남역" to response)
        )
        every { mockViewModel.uiState } returns MutableStateFlow(state)
        every { mockViewModel.processDirectionArrivalInfo(response) } returns emptyList()

        composeTestRule.setContent {
            BookmarkScreen(bookmarkViewModel = mockViewModel)
        }

        composeTestRule.waitUntil {
            composeTestRule.onAllNodes(hasText("강남역")).fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("강남역").assertIsDisplayed()
    }

    @Test
    fun bookmarkScreen_새로고침_버튼_클릭_테스트() {
        val mockViewModel = mockk<BookmarkViewModel>(relaxed = true)
        val state = BookmarkScreenState(
            bookmarks = setOf("강남역"),
            arrivalTimeMap = emptyMap()
        )
        every { mockViewModel.uiState } returns MutableStateFlow(state)
        justRun { mockViewModel.refreshArrivalInfo() }

        composeTestRule.setContent {
            BookmarkScreen(bookmarkViewModel = mockViewModel)
        }

        composeTestRule.onNodeWithContentDescription("새로고침").performClick()
        verify { mockViewModel.refreshArrivalInfo() }
    }

    @Test
    fun bookmarkScreen_도착_정보가_없는_역_표시_테스트() {
        val mockViewModel = mockk<BookmarkViewModel>(relaxed = true)
        val state = BookmarkScreenState(
            bookmarks = setOf("야탑역"),
            arrivalTimeMap = emptyMap()
        )
        every { mockViewModel.uiState } returns MutableStateFlow(state)

        composeTestRule.setContent {
            BookmarkScreen(bookmarkViewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("야탑역").assertIsDisplayed()
        composeTestRule.onNodeWithText("도착 정보 없음").assertIsDisplayed()
    }

    @Test
    fun bookmarkScreen_여러_북마크_표시_테스트() {
        val mockViewModel = mockk<BookmarkViewModel>(relaxed = true)
        val state = BookmarkScreenState(
            bookmarks = setOf("강남역", "야탑역", "수내역"),
            arrivalTimeMap = emptyMap()
        )
        every { mockViewModel.uiState } returns MutableStateFlow(state)

        composeTestRule.setContent {
            BookmarkScreen(bookmarkViewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("강남역").assertIsDisplayed()
        composeTestRule.onNodeWithText("야탑역").assertIsDisplayed()
        composeTestRule.onNodeWithText("수내역").assertIsDisplayed()
    }

    private fun createSampleResponse(stationName: String): SubwayArrivalResponse {
        return SubwayArrivalResponse(
            errorMessage = SubwayArrivalResponse.ErrorMessage(
                code = "INFO-000",
                developerMessage = "정상 처리되었습니다.",
                link = "",
                message = "정상 처리되었습니다.",
                status = 200,
                total = 1
            ),
            realtimeArrivalList = listOf(
                SubwayArrivalResponse.RealtimeArrival(
                    arvlCd = "1",
                    arvlMsg2 = "5분후 도착",
                    arvlMsg3 = stationName,
                    barvlDt = "0",
                    bstatnId = "13",
                    bstatnNm = "수내",
                    btrainNo = "4",
                    btrainSttus = "일반",
                    ordkey = "01000수내0",
                    recptnDt = "2024-02-06 14:13:21",
                    rowNum = 1,
                    selectedCount = 4,
                    statnFid = "1077006814",
                    statnId = "1077006813",
                    statnList = "1075075231,1077006813",
                    statnNm = stationName,
                    statnTid = "1077006812",
                    subwayId = "1077",
                    subwayList = "1075,1077",
                    totalCount = 1,
                    trainLineNm = "수내행 - 정자방면",
                    trnsitCo = "2",
                    updnLine = "상행"
                )
            )
        )
    }
}
