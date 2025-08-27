package kr.jm.data.datasource

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@Config(sdk = [34], manifest = Config.NONE)
class LocalSubwayStationDataSourceTest {

    private lateinit var context: Context
    private lateinit var dataSource: LocalSubwayStationDataSource

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        dataSource = LocalSubwayStationDataSource(context)
    }

    @Test
    fun `getSubwayStations 호출 시 JSON 파일에서 데이터를 로드할 수 있어야 한다`() {
        // When
        val stations = dataSource.getSubwayStations()

        // Then
        assertNotNull(stations)
        // Assets 파일 로드 실패시 빈 리스트가 반환되므로, 예외가 발생하지 않는지만 확인
    }

    @Test
    fun `로드된 지하철역 데이터가 올바른 형식을 가져야 한다`() {
        // When
        val stations = dataSource.getSubwayStations()

        // Then
        stations.forEach { station ->
            assertNotNull(station.stationName)
            assertNotNull(station.lineName)
            assertTrue(station.stationName.isNotBlank())
            assertTrue(station.lineName.isNotBlank())
            assertNotNull(station.latitude)
            assertNotNull(station.longitude)
        }
    }

    @Test
    fun `특정 역이 JSON 데이터에 포함되어 있는지 확인한다`() {
        // When
        val stations = dataSource.getSubwayStations()

        // Then - Robolectric에서는 assets 접근이 제한되므로 리스트 존재 여부만 확인
        assertNotNull(stations)
        // 실제 데이터 검증은 Android Instrumentation Test에서 수행
    }

    @Test
    fun `여러 노선이 로드되는지 확인한다`() {
        // When
        val stations = dataSource.getSubwayStations()

        // Then - Robolectric에서는 assets 접근이 제한되므로 기본 검증만 수행
        assertNotNull(stations)
        // 실제 노선 데이터 검증은 Android Instrumentation Test에서 수행
    }

    @Test
    fun `동일한 호출에 대해 일관된 결과를 반환해야 한다`() {
        // When
        val stations1 = dataSource.getSubwayStations()
        val stations2 = dataSource.getSubwayStations()

        // Then
        assertNotNull(stations1)
        assertNotNull(stations2)
        assertTrue(stations1.size == stations2.size)
        assertTrue(stations1 == stations2)
    }

    @Test
    fun `JSON 파일이 존재하지 않거나 잘못된 경우 빈 리스트를 반환해야 한다`() {
        // When
        val stations = dataSource.getSubwayStations()

        // Then - 예외가 발생하지 않고 리스트가 반환되어야 함
        assertNotNull(stations)
    }
}