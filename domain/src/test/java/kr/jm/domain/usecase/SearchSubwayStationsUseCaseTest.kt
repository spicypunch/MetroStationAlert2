package kr.jm.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kr.jm.domain.model.SubwayStation
import kr.jm.domain.repository.SubwayStationRepository
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class SearchSubwayStationsUseCaseTest {
    private lateinit var subwayStationRepository: SubwayStationRepository
    private lateinit var searchSubwayStationsUseCase: SearchSubwayStationsUseCase

    @Before
    fun setUp() {
        subwayStationRepository = mockk()
        searchSubwayStationsUseCase = SearchSubwayStationsUseCase(subwayStationRepository)
    }

    @Test
    fun `검색어와 일치하는 지하철 역 리스트를 반환해야 한다`() = runTest {
        // Given
        val query = "강남"
        val allStations = listOf(
            SubwayStation("1", "강남역", "2호선", 0.0, 0.0),
            SubwayStation("2", "역삼역", "2호선", 0.0, 0.0),
            SubwayStation("3", "강남구청역", "7호선", 0.0, 0.0)
        )
        val expectedFilteredList = listOf(
            SubwayStation("1", "강남역", "2호선", 0.0, 0.0),
            SubwayStation("3", "강남구청역", "7호선", 0.0, 0.0)
        )
        val repositoryResult = listOf(
            SubwayStation("1", "강남역", "2호선", 0.0, 0.0),
            SubwayStation("3", "강남구청역", "7호선", 0.0, 0.0)
        )
        coEvery { subwayStationRepository.searchStations(query, allStations) } returns repositoryResult

        // When
        val resultList = searchSubwayStationsUseCase(query, allStations)

        // Then
        assertEquals(expectedFilteredList, resultList)
        coVerify(exactly = 1) { subwayStationRepository.searchStations(query, allStations) }
    }

    @Test
    fun `검색어가 비어있으면 전체 리스트를 그대로 반환해야 한다`() = runTest {
        // Given
        val query = ""
        val allStations = listOf(
            SubwayStation("1", "강남역", "2호선", 0.0, 0.0),
            SubwayStation("2", "역삼역", "2호선", 0.0, 0.0)
        )
        coEvery { subwayStationRepository.searchStations(query, allStations) } returns allStations

        // When
        val resultList = searchSubwayStationsUseCase(query, allStations)

        // Then
        assertEquals(allStations, resultList)
        coVerify(exactly = 1) { subwayStationRepository.searchStations(query, allStations) }
    }
}