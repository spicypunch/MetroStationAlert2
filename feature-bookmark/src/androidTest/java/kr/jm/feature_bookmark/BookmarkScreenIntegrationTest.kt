package kr.jm.feature_bookmark

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kr.jm.domain.model.SubwayArrivalResponse
import kr.jm.domain.usecase.GetBookmarkUseCase
import kr.jm.domain.usecase.GetSubwayArrivalTimeUseCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookmarkScreenIntegrationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun bookmarkScreen_빈_북마크_상태_표시_테스트() {
        // Given
        val mockGetBookmarkUseCase = mockk<GetBookmarkUseCase>()
        val mockGetSubwayArrivalTimeUseCase = mockk<GetSubwayArrivalTimeUseCase>()

        every { mockGetBookmarkUseCase() } returns MutableStateFlow(emptySet())

        val mockViewModel = BookmarkViewModel(mockGetBookmarkUseCase, mockGetSubwayArrivalTimeUseCase)

        // When
        composeTestRule.setContent {
            BookmarkScreen(bookmarkViewModel = mockViewModel)
        }

        // Then
        composeTestRule.onNodeWithText("지하철 도착 정보🚊").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("새로고침").assertIsDisplayed()
    }

    @Test
    fun bookmarkScreen_북마크된_역_표시_테스트() {
        // Given
        val mockGetBookmarkUseCase = mockk<GetBookmarkUseCase>()
        val mockGetSubwayArrivalTimeUseCase = mockk<GetSubwayArrivalTimeUseCase>()

        val bookmarks = setOf("강남역")
        val mockResponse = createSampleResponse("강남역")

        every { mockGetBookmarkUseCase() } returns MutableStateFlow(bookmarks)
        coEvery { mockGetSubwayArrivalTimeUseCase("강남역") } returns mockResponse

        val mockViewModel = BookmarkViewModel(mockGetBookmarkUseCase, mockGetSubwayArrivalTimeUseCase)

        // When
        composeTestRule.setContent {
            BookmarkScreen(bookmarkViewModel = mockViewModel)
        }

        // Then
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodes(hasText("강남역")).fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("강남역").assertIsDisplayed()
        composeTestRule.onNodeWithText("지하철 도착 정보🚊").assertIsDisplayed()
    }

    @Test
    fun bookmarkScreen_새로고침_버튼_클릭_테스트() {
        // Given
        val mockGetBookmarkUseCase = mockk<GetBookmarkUseCase>()
        val mockGetSubwayArrivalTimeUseCase = mockk<GetSubwayArrivalTimeUseCase>()

        val bookmarks = setOf("강남역")
        val mockResponse = createSampleResponse("강남역")

        every { mockGetBookmarkUseCase() } returns MutableStateFlow(bookmarks)
        coEvery { mockGetSubwayArrivalTimeUseCase("강남역") } returns mockResponse

        val mockViewModel = BookmarkViewModel(mockGetBookmarkUseCase, mockGetSubwayArrivalTimeUseCase)

        // When
        composeTestRule.setContent {
            BookmarkScreen(bookmarkViewModel = mockViewModel)
        }

        // Then
        composeTestRule.onNodeWithContentDescription("새로고침").performClick()

        // UI가 여전히 표시되어야 함 (크래시 없음)
        composeTestRule.onNodeWithText("지하철 도착 정보🚊").assertIsDisplayed()
    }

    @Test
    fun bookmarkScreen_도착_정보가_없는_역_표시_테스트() {
        // Given
        val mockGetBookmarkUseCase = mockk<GetBookmarkUseCase>()
        val mockGetSubwayArrivalTimeUseCase = mockk<GetSubwayArrivalTimeUseCase>()

        val bookmarks = setOf("야탑역")

        every { mockGetBookmarkUseCase() } returns MutableStateFlow(bookmarks)
        coEvery { mockGetSubwayArrivalTimeUseCase("야탑역") } throws Exception("Network error")

        val mockViewModel = BookmarkViewModel(mockGetBookmarkUseCase, mockGetSubwayArrivalTimeUseCase)

        // When
        composeTestRule.setContent {
            BookmarkScreen(bookmarkViewModel = mockViewModel)
        }

        // Then
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodes(hasText("야탑역")).fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("야탑역").assertIsDisplayed()
        composeTestRule.onNodeWithText("도착 정보 없음").assertIsDisplayed()
    }

    @Test
    fun bookmarkScreen_여러_북마크_표시_테스트() {
        // Given
        val mockGetBookmarkUseCase = mockk<GetBookmarkUseCase>()
        val mockGetSubwayArrivalTimeUseCase = mockk<GetSubwayArrivalTimeUseCase>()

        val bookmarks = setOf("강남역", "야탑역", "수내역")
        val gangnamResponse = createSampleResponse("강남역")
        val yatapResponse = createSampleResponse("야탑역")
        val sunaeResponse = createSampleResponse("수내역")

        every { mockGetBookmarkUseCase() } returns MutableStateFlow(bookmarks)
        coEvery { mockGetSubwayArrivalTimeUseCase("강남역") } returns gangnamResponse
        coEvery { mockGetSubwayArrivalTimeUseCase("야탑역") } returns yatapResponse
        coEvery { mockGetSubwayArrivalTimeUseCase("수내역") } returns sunaeResponse

        val mockViewModel = BookmarkViewModel(mockGetBookmarkUseCase, mockGetSubwayArrivalTimeUseCase)

        // When
        composeTestRule.setContent {
            BookmarkScreen(bookmarkViewModel = mockViewModel)
        }

        // Then
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodes(hasText("강남역")).fetchSemanticsNodes().isNotEmpty() &&
            composeTestRule.onAllNodes(hasText("야탑역")).fetchSemanticsNodes().isNotEmpty() &&
            composeTestRule.onAllNodes(hasText("수내역")).fetchSemanticsNodes().isNotEmpty()
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