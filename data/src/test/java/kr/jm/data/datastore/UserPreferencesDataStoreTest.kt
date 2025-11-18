package kr.jm.data.datastore

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.robolectric.RobolectricTestRunner
import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class UserPreferencesDataStoreTest {

    private lateinit var dataStore: UserPreferencesDataStore
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        dataStore = UserPreferencesDataStore(context)
    }


    @Test
    fun `addBookmark 성공 시 Result success를 반환해야 한다`() = runTest {
        // Given
        val stationName = "강남역"

        // When
        val result = dataStore.addBookmark(stationName)

        // Then
        assertTrue(result.isSuccess, "addBookmark should succeed")
        assertEquals(stationName, result.getOrNull(), "Should return the station name")
    }

    @Test
    fun `addBookmark 후 getBookmarks에서 추가된 북마크를 확인할 수 있어야 한다`() = runTest {
        // Given
        val stationName = "강남역"

        // When
        dataStore.addBookmark(stationName)

        // Then
        dataStore.getBookmarks().test {
            val bookmarks = awaitItem()
            assertTrue(bookmarks.contains(stationName), "Bookmarks should contain the added station")
        }
    }

    @Test
    fun `여러 북마크 추가 시 모두 저장되어야 한다`() = runTest {
        // Given
        val stations = listOf("강남역", "역삼역", "선릉역")

        // When
        stations.forEach { station ->
            dataStore.addBookmark(station)
        }

        // Then
        dataStore.getBookmarks().test {
            val bookmarks = awaitItem()
            stations.forEach { station ->
                assertTrue(bookmarks.contains(station), "Should contain $station")
            }
            assertEquals(stations.size, bookmarks.size, "Should have all stations")
        }
    }

    @Test
    fun `removeBookmark 성공 시 Result success를 반환해야 한다`() = runTest {
        // Given
        val stationName = "강남역"
        dataStore.addBookmark(stationName)

        // When
        val result = dataStore.removeBookmark(stationName)

        // Then
        assertTrue(result.isSuccess, "removeBookmark should succeed")
        assertEquals(stationName, result.getOrNull(), "Should return the station name")
    }

    @Test
    fun `removeBookmark 후 해당 북마크가 제거되어야 한다`() = runTest {
        // Given
        val stationName = "강남역"
        dataStore.addBookmark(stationName)

        // When
        dataStore.removeBookmark(stationName)

        // Then
        dataStore.getBookmarks().test {
            val bookmarks = awaitItem()
            assertFalse(bookmarks.contains(stationName), "Should not contain removed station")
        }
    }

    @Test
    fun `존재하지 않는 북마크 제거 시에도 성공해야 한다`() = runTest {
        // Given
        val stationName = "존재하지않는역"

        // When
        val result = dataStore.removeBookmark(stationName)

        // Then
        assertTrue(result.isSuccess, "Should succeed even if station doesn't exist")
    }

    @Test
    fun `addAlertStation 성공 시 Result success를 반환해야 한다`() = runTest {
        // Given
        val stationName = "강남역"

        // When
        val result = dataStore.addAlertStation(stationName)

        // Then
        assertTrue(result.isSuccess, "addAlertStation should succeed")
        assertEquals(stationName, result.getOrNull(), "Should return the station name")
    }

    @Test
    fun `addAlertStation 후 getAddedAlertStation에서 설정된 역을 확인할 수 있어야 한다`() = runTest {
        // Given
        val stationName = "강남역"

        // When
        dataStore.addAlertStation(stationName)

        // Then
        dataStore.getAddedAlertStation().test {
            val alertStation = awaitItem()
            assertEquals(stationName, alertStation, "Should return the set alert station")
        }
    }

    @Test
    fun `새로운 알림역 설정 시 기존 알림역을 덮어써야 한다`() = runTest {
        // Given
        val firstStation = "강남역"
        val secondStation = "역삼역"

        // When
        dataStore.addAlertStation(firstStation)
        dataStore.addAlertStation(secondStation)

        // Then
        dataStore.getAddedAlertStation().test {
            val alertStation = awaitItem()
            assertEquals(secondStation, alertStation, "Should overwrite with new station")
        }
    }

    @Test
    fun `addRecentSearch로 검색어를 추가할 수 있어야 한다`() = runTest {
        // Given
        val searchQuery = "강남"

        // When
        dataStore.addRecentSearch(searchQuery)

        // Then
        dataStore.getRecentSearches().test {
            val searches = awaitItem()
            assertTrue(searches.contains(searchQuery), "Should contain the search query")
        }
    }

    @Test
    fun `최근 검색어는 최신 순으로 정렬되어야 한다`() = runTest {
        // Given
        val queries = listOf("강남", "역삼", "선릉")

        // When
        queries.forEach { query ->
            dataStore.addRecentSearch(query)
        }

        // Then
        dataStore.getRecentSearches().test {
            val searches = awaitItem()
            assertEquals(queries.reversed(), searches, "Should be in reverse order (latest first)")
        }
    }

    @Test
    fun `중복된 검색어 추가 시 기존 항목을 제거하고 최상단에 추가해야 한다`() = runTest {
        // Given
        val queries = listOf("강남", "역삼", "선릉")
        queries.forEach { dataStore.addRecentSearch(it) }

        // When - 기존에 있던 "강남"을 다시 검색
        dataStore.addRecentSearch("강남")

        // Then
        dataStore.getRecentSearches().test {
            val searches = awaitItem()
            assertEquals("강남", searches.first(), "Should be at the top")
            assertEquals(3, searches.size, "Should not increase the size")
            assertTrue(searches.indexOf("강남") == 0, "Should be at index 0")
        }
    }

    @Test
    fun `clearRecentSearches 호출 시 모든 검색 기록이 삭제되어야 한다`() = runTest {
        // Given
        val queries = listOf("강남", "역삼", "선릉")
        queries.forEach { dataStore.addRecentSearch(it) }

        // When
        dataStore.clearRecentSearches()

        // Then
        dataStore.getRecentSearches().test {
            val searches = awaitItem()
            assertTrue(searches.isEmpty(), "Should be empty after clearing")
        }
    }

    @Test
    fun `getBookmarks는 초기에 빈 Set을 반환해야 한다`() = runTest {
        // When & Then
        dataStore.getBookmarks().test {
            val bookmarks = awaitItem()
            assertTrue(bookmarks.isEmpty(), "Initial bookmarks should be empty")
        }
    }


    @Test
    fun `getRecentSearches는 초기에 빈 리스트를 반환해야 한다`() = runTest {
        // When & Then
        dataStore.getRecentSearches().test {
            val searches = awaitItem()
            assertTrue(searches.isEmpty(), "Initial searches should be empty")
        }
    }
}
