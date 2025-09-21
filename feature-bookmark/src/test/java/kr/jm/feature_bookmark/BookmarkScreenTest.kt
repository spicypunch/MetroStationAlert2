package kr.jm.feature_bookmark

import kr.jm.domain.model.SubwayArrivalResponse
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class BookmarkScreenTest {

    @Test
    fun `BookmarkScreenState 초기값이 올바르게 설정되어야 한다`() {
        // Given & When
        val state = BookmarkScreenState()

        // Then
        assertEquals(emptySet(), state.bookmarks)
        assertEquals(emptyMap(), state.arrivalTimeMap)
        assertEquals(false, state.isLoading)
        assertEquals(null, state.error)
    }

    @Test
    fun `BookmarkScreenState 커스텀 값으로 생성되어야 한다`() {
        // Given
        val bookmarks = setOf("강남역", "야탑역")
        val arrivalTimeMap = mapOf(
            "강남역" to createSampleResponse("강남역")
        )

        // When
        val state = BookmarkScreenState(
            bookmarks = bookmarks,
            arrivalTimeMap = arrivalTimeMap,
            isLoading = true,
            error = "네트워크 오류"
        )

        // Then
        assertEquals(bookmarks, state.bookmarks)
        assertEquals(arrivalTimeMap, state.arrivalTimeMap)
        assertEquals(true, state.isLoading)
        assertEquals("네트워크 오류", state.error)
    }

    @Test
    fun `DirectionArrivalInfo 객체가 올바르게 생성되어야 한다`() {
        // Given
        val direction = "왕십리행"
        val upDownLine = "상행"
        val arrivals = listOf(
            ArrivalInfo(
                message = "5분후 도착",
                trainLineNm = "왕십리행 - 정자방면",
                receivedTime = "2024-02-06 14:13:21"
            )
        )

        // When
        val directionInfo = DirectionArrivalInfo(
            direction = direction,
            upDownLine = upDownLine,
            arrivals = arrivals
        )

        // Then
        assertEquals(direction, directionInfo.direction)
        assertEquals(upDownLine, directionInfo.upDownLine)
        assertEquals(arrivals, directionInfo.arrivals)
        assertEquals(1, directionInfo.arrivals.size)
    }

    @Test
    fun `ArrivalInfo 객체가 올바르게 생성되어야 한다`() {
        // Given
        val message = "전역 출발"
        val trainLineNm = "수내행 - 정자방면"
        val receivedTime = "2024-02-06 14:13:21"

        // When
        val arrivalInfo = ArrivalInfo(
            message = message,
            trainLineNm = trainLineNm,
            receivedTime = receivedTime
        )

        // Then
        assertEquals(message, arrivalInfo.message)
        assertEquals(trainLineNm, arrivalInfo.trainLineNm)
        assertEquals(receivedTime, arrivalInfo.receivedTime)
    }

    @Test
    fun `빈 북마크 리스트로 상태가 생성되어야 한다`() {
        // Given & When
        val state = BookmarkScreenState(bookmarks = emptySet())

        // Then
        assertTrue(state.bookmarks.isEmpty())
        assertTrue(state.arrivalTimeMap.isEmpty())
    }

    @Test
    fun `여러 역의 도착 정보를 포함한 상태가 생성되어야 한다`() {
        // Given
        val bookmarks = setOf("강남역", "야탑역", "수내역")
        val arrivalTimeMap = mapOf(
            "강남역" to createSampleResponse("강남역"),
            "야탑역" to createSampleResponse("야탑역")
        )

        // When
        val state = BookmarkScreenState(
            bookmarks = bookmarks,
            arrivalTimeMap = arrivalTimeMap
        )

        // Then
        assertEquals(3, state.bookmarks.size)
        assertEquals(2, state.arrivalTimeMap.size)
        assertTrue(state.bookmarks.contains("강남역"))
        assertTrue(state.bookmarks.contains("야탑역"))
        assertTrue(state.bookmarks.contains("수내역"))
        assertNotNull(state.arrivalTimeMap["강남역"])
        assertNotNull(state.arrivalTimeMap["야탑역"])
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
                    arvlMsg2 = "$stationName 도착",
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