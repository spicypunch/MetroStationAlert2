package kr.jm.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kr.jm.domain.model.SubwayArrivalResponse
import kr.jm.domain.repository.OpenApiRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.Before

class GetSubwayArrivalTimeUseCaseTest {

    private lateinit var repository: OpenApiRepository
    private lateinit var getSubwayArrivalTimeUseCase: GetSubwayArrivalTimeUseCase

    @Before
    fun setUp() {
        repository = mockk()
        getSubwayArrivalTimeUseCase = GetSubwayArrivalTimeUseCase(repository)
    }

    @Test
    fun `역 이름으로 지하철 도착 정보를 요청하고 응답을 반환해야 한다`() = runTest {
        // Given
        val stationName = "야탑"
        val expectedResponse = SubwayArrivalResponse(
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
                    arvlMsg2 = "야탑 도착",
                    arvlMsg3 = "야탑",
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
                    statnNm = "야탑",
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
        coEvery { repository.getSubwayArrivalTime(stationName) } returns expectedResponse

        // When
        val result = getSubwayArrivalTimeUseCase(stationName)

        // Then
        assertEquals(expectedResponse, result)
        coVerify(exactly = 1) { repository.getSubwayArrivalTime(stationName) }
    }

    @Test
    fun `빈 역 이름으로 요청해도 repository의 응답을 그대로 반환해야 한다`() = runTest {
        // Given
        val stationName = ""
        val expectedResponse = SubwayArrivalResponse(
            errorMessage = SubwayArrivalResponse.ErrorMessage(
                code = "ERROR-001",
                developerMessage = "잘못된 요청입니다.",
                link = "",
                message = "잘못된 요청입니다.",
                status = 400,
                total = 0
            ),
            realtimeArrivalList = emptyList()
        )
        coEvery { repository.getSubwayArrivalTime(stationName) } returns expectedResponse

        // When
        val result = getSubwayArrivalTimeUseCase(stationName)

        // Then
        assertEquals(expectedResponse, result)
        coVerify(exactly = 1) { repository.getSubwayArrivalTime(stationName) }
    }
}