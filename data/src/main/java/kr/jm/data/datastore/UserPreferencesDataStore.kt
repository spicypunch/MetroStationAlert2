package kr.jm.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
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
    }

    suspend fun addBookmark(stationName: String): Result<String> {
        return try {
            context.dataStore.edit { preferences ->
                val currentBookmarks =
                    preferences[PreferencesKeys.BOOKMARKED_STATIONS] ?: emptySet()
                preferences[PreferencesKeys.BOOKMARKED_STATIONS] = currentBookmarks + stationName
            }
            Result.success(stationName)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeBookmark(stationName: String) {
        context.dataStore.edit { preferences ->
            val currentBookmarks = preferences[PreferencesKeys.BOOKMARKED_STATIONS] ?: emptySet()
            preferences[PreferencesKeys.BOOKMARKED_STATIONS] = currentBookmarks - stationName
        }
    }

    fun getBookmarks(): Flow<Set<String>> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.BOOKMARKED_STATIONS] ?: emptySet()
        }
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
}