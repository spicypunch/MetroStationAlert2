package kr.jm.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesDataStore @Inject constructor(
    private val context: Context
) {
    private object PreferencesKeys {
        val BOOKMARKED_STATIONS = stringSetPreferencesKey("bookmarked_stations")
        val RECENT_SEARCHES = stringPreferencesKey("recent_searches")
        val ALERT_STATION = stringPreferencesKey("alert_station")
        val LATITUDE = doublePreferencesKey("latitude")
        val LONGITUDE = doublePreferencesKey("longitude")
        val DISTANCE = floatPreferencesKey("distance")
        val NOTI_TITLE = stringPreferencesKey("noti_title")
        val NOTI_CONTENT = stringPreferencesKey("noti_content")
    }

    suspend fun addBookmark(stationKey: String): Result<String> {
        return try {
            context.dataStore.edit { preferences ->
                val currentBookmarks =
                    preferences[PreferencesKeys.BOOKMARKED_STATIONS] ?: emptySet()
                preferences[PreferencesKeys.BOOKMARKED_STATIONS] = currentBookmarks + stationKey
            }
            Result.success(stationKey)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeBookmark(stationKey: String): Result<String> {
        return try {
            context.dataStore.edit { preferences ->
                val currentBookmarks =
                    preferences[PreferencesKeys.BOOKMARKED_STATIONS] ?: emptySet()
                preferences[PreferencesKeys.BOOKMARKED_STATIONS] = currentBookmarks - stationKey
            }
            Result.success(stationKey)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getBookmarks(): Flow<Set<String>> {
        return context.dataStore.data
            .map { preferences ->
                preferences[PreferencesKeys.BOOKMARKED_STATIONS] ?: emptySet()
            }
            .distinctUntilChanged()
    }

    suspend fun addAlertStation(stationName: String): Result<String> {
        return try {
            context.dataStore.edit { preferences ->
                preferences[PreferencesKeys.ALERT_STATION] = stationName
            }
            Result.success(stationName)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAddedAlertStation(): Flow<String?> {
        return context.dataStore.data
            .map { preferences ->
                preferences[PreferencesKeys.ALERT_STATION]
            }
            .distinctUntilChanged()
    }

    suspend fun addRecentSearch(query: String) {
        context.dataStore.edit { preferences ->
            val currentSearches =
                preferences[PreferencesKeys.RECENT_SEARCHES]?.split(",")?.toMutableList()
                    ?: mutableListOf()

            // Remove if already exists
            currentSearches.remove(query)
            // Add to front
            currentSearches.add(0, query)
            // Keep only last 10 searches
            if (currentSearches.size > 10) {
                currentSearches.removeAt(currentSearches.size - 1)
            }

            preferences[PreferencesKeys.RECENT_SEARCHES] = currentSearches.joinToString(",")
        }
    }

    fun getRecentSearches(): Flow<List<String>> {
        return context.dataStore.data.map { preferences ->
            val searches = preferences[PreferencesKeys.RECENT_SEARCHES]
            if (searches.isNullOrBlank()) emptyList() else searches.split(",")
        }
    }

    suspend fun clearRecentSearches() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.RECENT_SEARCHES)
        }
    }

    suspend fun saveLocation(latitude: Double, longitude: Double) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LATITUDE] = latitude
            preferences[PreferencesKeys.LONGITUDE] = longitude
        }
    }

    fun getLatitude(): Flow<Double> {
        return context.dataStore.data
            .map { preferences ->
                preferences[PreferencesKeys.LATITUDE] ?: 0.0
            }
            .distinctUntilChanged()
    }

    fun getLongitude(): Flow<Double> {
        return context.dataStore.data
            .map { preferences ->
                preferences[PreferencesKeys.LONGITUDE] ?: 0.0
            }
            .distinctUntilChanged()
    }

    suspend fun saveDistance(distance: Float) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DISTANCE] = distance
        }
    }

    fun getDistance(): Flow<Float> {
        return context.dataStore.data
            .map { preferences ->
                preferences[PreferencesKeys.DISTANCE] ?: 1.0f
            }
            .distinctUntilChanged()
    }

    suspend fun saveNotificationSettings(title: String, content: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTI_TITLE] = title
            preferences[PreferencesKeys.NOTI_CONTENT] = content
        }
    }

    fun getNotiTitle(): Flow<String> {
        return context.dataStore.data
            .map { preferences ->
                preferences[PreferencesKeys.NOTI_TITLE] ?: "하차 알림"
            }
            .distinctUntilChanged()
    }

    fun getNotiContent(): Flow<String> {
        return context.dataStore.data
            .map { preferences ->
                preferences[PreferencesKeys.NOTI_CONTENT] ?: "곧 하차하실 역입니다."
            }
            .distinctUntilChanged()
    }

}
