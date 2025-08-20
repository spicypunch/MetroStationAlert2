package kr.jm.domain.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kr.jm.domain.model.SubwayStation
import kr.jm.domain.repository.SubwayStationRepository
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetSubwayStationsUseCaseTest {
    private lateinit var subwayStationRepository: SubwayStationRepository
    private lateinit var getSubwayStationsUseCase: GetSubwayStationsUseCase

    @Before
    fun setUp() {
        subwayStationRepository = mockk()
        getSubwayStationsUseCase = GetSubwayStationsUseCase(subwayStationRepository)
    }

    @Test
    fun `지하철역 목록을 Flow로 반환해야 한다`() = runTest {
        // Given
        val fakeStations = listOf(
            SubwayStation("1", "강남역", "2호선", 37.0, 127.0, false),
            SubwayStation("2", "역삼역", "2호선", 37.1, 127.1, true)
        )
        val expectedFlow = flowOf(fakeStations)
        every { subwayStationRepository.getAllStations() } returns expectedFlow

        // When
        val resultFlow = getSubwayStationsUseCase()

        // Then
        assertEquals(fakeStations, resultFlow.first())
        verify(exactly = 1) { subwayStationRepository.getAllStations() }
    }

    @Test
    fun `지하철역 목록이 비어있을 경우 빈 리스트를 포함한 Flow를 반환해야 한다`() = runTest {
        // Given
        val emptyList = emptyList<SubwayStation>()
        val expectedFlow = flowOf(emptyList)
        every { subwayStationRepository.getAllStations() } returns expectedFlow

        // When
        val resultFlow = getSubwayStationsUseCase()

        // Then
        assertEquals(emptyList, resultFlow.first())
        verify(exactly = 1) { subwayStationRepository.getAllStations() }
    }
}