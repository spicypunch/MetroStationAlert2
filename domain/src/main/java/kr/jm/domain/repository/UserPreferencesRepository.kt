package kr.jm.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    suspend fun addBookmark(stationKey: String): Result<String>
    suspend fun removeBookmark(stationKey: String): Result<String>
    fun getBookmarks(): Flow<Set<String>>
    suspend fun addAlertStationWithLocation(stationName: String, latitude: Double, longitude: Double): Result<String>
    fun getAddedAlertStation(): Flow<String?>
    suspend fun addRecentSearch(query: String)
    fun getRecentSearches(): Flow<List<String>>
    suspend fun clearRecentSearches()
    suspend fun saveDistance(distance: Float): Result<Unit>
    fun getDistance(): Flow<Float>
    suspend fun saveNotificationSettings(title: String, content: String): Result<Unit>
    fun getNotiTitle(): Flow<String>
    fun getNotiContent(): Flow<String>
}