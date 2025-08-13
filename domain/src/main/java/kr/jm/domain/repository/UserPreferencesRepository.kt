package kr.jm.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    suspend fun addBookmark(stationId: String)
    suspend fun removeBookmark(stationId: String)
    fun getBookmarks(): Flow<Set<String>>
    suspend fun addRecentSearch(query: String)
    fun getRecentSearches(): Flow<List<String>>
    suspend fun clearRecentSearches()
}