package kr.jm.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    suspend fun addBookmark(stationName: String): Result<String>
    suspend fun removeBookmark(stationName: String): Result<String>
    fun getBookmarks(): Flow<Set<String>>
    suspend fun addAlertStation(stationName: String): Result<String>
    fun getAddedAlertStation(): Flow<String?>
    suspend fun addRecentSearch(query: String)
    fun getRecentSearches(): Flow<List<String>>
    suspend fun clearRecentSearches()
}