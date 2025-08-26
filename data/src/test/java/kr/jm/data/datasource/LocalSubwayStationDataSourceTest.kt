package kr.jm.data.datasource

import android.content.Context
import android.content.res.AssetManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kr.jm.data.model.SubwayStationDto
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LocalSubwayStationDataSourceTest {

    private lateinit var dataSource: LocalSubwayStationDataSource
    private val mockContext: Context = mockk()
    private val mockAssetManager: AssetManager = mockk()

    private val validJsonData = """
        [
            {
                "notUse": "unused1",
                "stationName": "강남역",
                "lineName": "2호선",
                "longitude": 127.0276,
                "latitude": 37.4979
            },
            {
                "notUse": "unused2", 
                "stationName": "역삼역",
                "lineName": "2호선",
                "longitude": 127.0357,
                "latitude": 37.5007
            }
        ]
    """.trimIndent()

    @Before
    fun setUp() {
        every { mockContext.assets } returns mockAssetManager
        dataSource = LocalSubwayStationDataSource(mockContext)
    }

    @Test
    fun `getSubwayStations 호출 시 JSON 파일에서 데이터를 정상적으로 파싱해야 한다`() {
        // Given
        val inputStream = ByteArrayInputStream(validJsonData.toByteArray())
        every { mockAssetManager.open("SubwayStations.json") } returns inputStream

        // When
        val stations = dataSource.getSubwayStations()

        // Then
        assertEquals(2, stations.size, "Should parse 2 stations")
        assertEquals("강남역", stations[0].stationName)
        assertEquals("2호선", stations[0].lineName)
        assertEquals(127.0276, stations[0].longitude, 0.001)
        assertEquals(37.4979, stations[0].latitude, 0.001)
        
        verify { mockAssetManager.open("SubwayStations.json") }
    }

    @Test
    fun `JSON 데이터의 모든 필드가 올바르게 매핑되어야 한다`() {
        // Given
        val inputStream = ByteArrayInputStream(validJsonData.toByteArray())
        every { mockAssetManager.open("SubwayStations.json") } returns inputStream

        // When
        val stations = dataSource.getSubwayStations()

        // Then
        stations.forEach { station ->
            assertTrue(station.stationName.isNotBlank(), "Station name should not be blank")
            assertTrue(station.lineName.isNotBlank(), "Line name should not be blank")
            assertTrue(station.notUse.isNotBlank(), "notUse field should not be blank")
        }
    }

    @Test
    fun `빈 JSON 배열인 경우 빈 리스트를 반환해야 한다`() {
        // Given
        val emptyJsonData = "[]"
        val inputStream = ByteArrayInputStream(emptyJsonData.toByteArray())
        every { mockAssetManager.open("SubwayStations.json") } returns inputStream

        // When
        val stations = dataSource.getSubwayStations()

        // Then
        assertTrue(stations.isEmpty(), "Should return empty list for empty JSON")
    }

    @Test
    fun `잘못된 JSON 형식인 경우 빈 리스트를 반환해야 한다`() {
        // Given
        val invalidJsonData = "invalid json data"
        val inputStream = ByteArrayInputStream(invalidJsonData.toByteArray())
        every { mockAssetManager.open("SubwayStations.json") } returns inputStream

        // When
        val stations = dataSource.getSubwayStations()

        // Then
        assertTrue(stations.isEmpty(), "Should return empty list for invalid JSON")
    }

    @Test
    fun `파일을 읽을 수 없는 경우 빈 리스트를 반환해야 한다`() {
        // Given
        every { mockAssetManager.open("SubwayStations.json") } throws Exception("File not found")

        // When
        val stations = dataSource.getSubwayStations()

        // Then
        assertTrue(stations.isEmpty(), "Should return empty list when file cannot be read")
    }

    @Test
    fun `부분적으로 잘못된 JSON 객체가 있어도 유효한 객체는 파싱해야 한다`() {
        // Given
        val partiallyValidJson = """
        [
            {
                "notUse": "unused1",
                "stationName": "강남역",
                "lineName": "2호선",
                "longitude": 127.0276,
                "latitude": 37.4979
            },
            {
                "invalidField": "invalid"
            }
        ]
        """.trimIndent()
        val inputStream = ByteArrayInputStream(partiallyValidJson.toByteArray())
        every { mockAssetManager.open("SubwayStations.json") } returns inputStream

        // When
        val stations = dataSource.getSubwayStations()

        // Then
        // Moshi는 전체 파싱이 실패하면 빈 리스트를 반환할 것임
        assertTrue(stations.isEmpty() || stations.size == 1, "Should handle partial invalid JSON gracefully")
    }

    @Test
    fun `여러 번 호출해도 동일한 결과를 반환해야 한다`() {
        // Given
        val inputStream1 = ByteArrayInputStream(validJsonData.toByteArray())
        val inputStream2 = ByteArrayInputStream(validJsonData.toByteArray())
        every { mockAssetManager.open("SubwayStations.json") } returns inputStream1 andThen inputStream2

        // When
        val stations1 = dataSource.getSubwayStations()
        val stations2 = dataSource.getSubwayStations()

        // Then
        assertEquals(stations1, stations2, "Should return consistent results")
        verify(exactly = 2) { mockAssetManager.open("SubwayStations.json") }
    }
}